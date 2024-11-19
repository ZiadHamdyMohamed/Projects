package game.engine;

import java.io.*;
import java.util.*;

import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;
import game.engine.weapons.factory.*;
import game.engine.titans.*;
import game.engine.lanes.*;
import game.engine.base.*;
import static game.engine.dataloader.DataLoader.readTitanRegistry;

public class Battle {


    private static final int[][] PHASES_APPROACHING_TITANS = {
            { 1, 1, 1, 2, 1, 3, 4 },
            { 2, 2, 2, 1, 3, 3, 4 },
            { 4, 4, 4, 4, 4, 4, 4 }
    };
    private static final int WALL_BASE_HEALTH = 10000;
    private int numberOfTurns;
    private int resourcesGathered;
    private BattlePhase battlePhase;
    private int numberOfTitansPerTurn;
    private int score;
    private int titanSpawnDistance;
    private final WeaponFactory weaponFactory;
    private final HashMap<Integer, TitanRegistry> titansArchives;
    private final ArrayList<Titan> approachingTitans;
    private final PriorityQueue<Lane> lanes;
    private final ArrayList<Lane> originalLanes;


    public Battle(int numberOfTurns, int score, int titanSpawnDistance, int initialNumOfLanes, int initialResourcesPerLane) throws IOException {
        this.numberOfTurns = numberOfTurns;
        this.score = score;
        this.titanSpawnDistance = titanSpawnDistance;
        this.numberOfTitansPerTurn = 1;
        this.battlePhase = BattlePhase.EARLY;
        this.resourcesGathered = initialNumOfLanes * initialResourcesPerLane;
        this.weaponFactory = new WeaponFactory();
        this.titansArchives =  readTitanRegistry();
        this.approachingTitans = new ArrayList<Titan>();
        this.lanes = new PriorityQueue<Lane>();
        this.originalLanes = new ArrayList<Lane>();
        initializeLanes(initialNumOfLanes);

    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public int getResourcesGathered() {
        return resourcesGathered;
    }

    public BattlePhase getBattlePhase() {
        return battlePhase;
    }

    public int getNumberOfTitansPerTurn() {
        return numberOfTitansPerTurn;
    }

    public int getScore() {
        return score;
    }

    public int getTitanSpawnDistance() {
        return titanSpawnDistance;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public void setResourcesGathered(int resourcesGathered) {
        this.resourcesGathered = resourcesGathered;
    }

    public void setBattlePhase(BattlePhase battlePhase) {
        this.battlePhase = battlePhase;
    }

    public void setNumberOfTitansPerTurn(int numberOfTitansPerTurn) {
        this.numberOfTitansPerTurn = numberOfTitansPerTurn;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTitanSpawnDistance(int titanSpawnDistance) {
        this.titanSpawnDistance = titanSpawnDistance;
    }




    public static int[][] getPhasesApproachingTitans() {
        return PHASES_APPROACHING_TITANS;
    }

    public static int getWallBaseHealth() {
        return WALL_BASE_HEALTH;
    }

    public WeaponFactory getWeaponFactory() {
        return weaponFactory;
    }

    public HashMap<Integer, TitanRegistry> getTitansArchives() {
        return titansArchives;
    }

    public ArrayList<Titan> getApproachingTitans() {
        return approachingTitans;
    }

    public PriorityQueue<Lane> getLanes() {
        return lanes;
    }

    public ArrayList<Lane> getOriginalLanes() {
        return originalLanes;
    }
    private void initializeLanes(int numOfLanes) {
        for (int i = 0; i < numOfLanes; i++) {
            Wall wall = new Wall(WALL_BASE_HEALTH);
            Lane lane = new Lane(wall);
            this.lanes.add(lane);
            this.originalLanes.add(lane);
        }
    }


    public void refillApproachingTitans() {
        if(!approachingTitans.isEmpty())return;
        BattlePhase currentPhase = getBattlePhase();
        int[] phaseCodes = PHASES_APPROACHING_TITANS[currentPhase.ordinal()];
        approachingTitans.clear();
        for (int code : phaseCodes) {
            TitanRegistry titanRegistry = titansArchives.get(code);
            if (titanRegistry != null) {
                approachingTitans.add(titanRegistry.spawnTitan(titanSpawnDistance));
            }
        }
    }

    public void purchaseWeapon(int weaponCode, Lane lane) throws InsufficientResourcesException, InvalidLaneException {

        if(!lanes.contains(lane)||lane == null || lane.isLaneLost()){
            throw new InvalidLaneException();
        }
        Weapon weapon =weaponFactory.buyWeapon(resourcesGathered,weaponCode).getWeapon();
        setResourcesGathered(getResourcesGathered()-weaponFactory.getWeaponShop().get(weaponCode).getPrice());
        lane.addWeapon(weapon);
        performTurn();
    }

    public void passTurn() {
      performTurn();
    }



    private void addTurnTitansToLane() {
        for (int i = 0; i < numberOfTitansPerTurn; i++) {
                refillApproachingTitans();
            Titan titan = approachingTitans.remove(0);
            Lane leastDangerousLane = lanes.peek();
            if (leastDangerousLane != null) {
                leastDangerousLane.addTitan(titan);
            }
        }

    }

    private void moveTitans() {
        for (Lane lane : lanes) {
            lane.moveLaneTitans();
        }

    }

    private int performWeaponsAttacks() {
        int totalResources = 0;
        for (Lane lane : lanes) {
            if (!lane.isLaneLost()) {
                int laneResources = lane.performLaneWeaponsAttacks();
                totalResources += laneResources;
                this.resourcesGathered += laneResources;
                this.score += laneResources;
            }
        }
        return totalResources;
    }

    private int performTitansAttacks() {
        int wallResourcesValue = 0;
        Iterator<Lane> iterator = lanes.iterator();
        while (iterator.hasNext()) {
            Lane lane = iterator.next();
            wallResourcesValue += lane.performLaneTitansAttacks();
            if (lane.isLaneLost()) {
                iterator.remove();
            }
        }
        return wallResourcesValue;
    }
    private void updateLanesDangerLevels() {
        int size = lanes.size();
        ArrayList<Lane> tempLanes = new ArrayList<>();
    while (!lanes.isEmpty()){
        Lane lane= lanes.poll();
        if (!lane.isLaneLost()){
            lane.updateLaneDangerLevel();
            tempLanes.add(lane);
        }
    }
        lanes.addAll(tempLanes);
    }




    private void finalizeTurns() {
        numberOfTurns++;
        if (numberOfTurns < 15) {
            battlePhase = BattlePhase.EARLY;
        } else if (numberOfTurns < 30) {
            battlePhase = BattlePhase.INTENSE;
        } else {
            battlePhase = BattlePhase.GRUMBLING;
            if (numberOfTurns % 5 == 0 && numberOfTurns > 30) {
                numberOfTitansPerTurn *= 2;
            }
        }
    }



    private void performTurn() {

        moveTitans();
        performWeaponsAttacks();
        performTitansAttacks();
        addTurnTitansToLane();
        updateLanesDangerLevels();
        finalizeTurns();
    }



    public boolean isGameOver() {
        for (Lane lane : lanes) {
            if (!lane.isLaneLost()) {
                return false;
            }
        }
        return true;
    }
}
