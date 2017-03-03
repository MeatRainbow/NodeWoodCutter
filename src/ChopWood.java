import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

public class ChopWood extends Task {

    private Predicate<RS2Object> suitableTree = tree ->
            tree != null &&
                    NodeWoodCutter.WOOD_AREA.contains(tree) &&
                    tree.getName().equalsIgnoreCase("oak") &&
                    tree.hasAction("Chop down") &&
                    peopleAroundEntity(tree) <= 2;

    private Predicate<RS2Object> secondBestTree = tree ->
            tree != null &&
                    NodeWoodCutter.WOOD_AREA.contains(tree) &&
                    tree.hasAction("Chop down") ;



    public ChopWood(MethodProvider api) {
        super(api);
    }


    @Override
    public boolean canProcess() {

        return !api.inventory.isFull() && NodeWoodCutter.WOOD_AREA.contains(api.myPlayer())
                && !api.myPlayer().isAnimating();

    }

    @Override
    public void process() {

        NodeWoodCutter.state = "State: Finding tree to cut";

        List<RS2Object> tree = api.getObjects().getAll().stream().filter(suitableTree).collect(Collectors.toList());
        NodeWoodCutter.gainedExp = api.getExperienceTracker().getGainedXP(Skill.WOODCUTTING);
        if (tree != null){
            tree.sort(Comparator.<RS2Object>comparingInt(a -> api.getMap().realDistance(a))
                    .thenComparingInt(b -> api.getMap().realDistance(b)));
            NodeWoodCutter.currentTree = tree.get(0);
            if (tree.get(0).interact("Chop down")){
                NodeWoodCutter.state = "Chopping Tree";
                Timing.waitCondition(() -> NodeWoodCutter.gainedExp != api.getExperienceTracker().getGainedXP(Skill.WOODCUTTING)
                                , api.random(3000, 6000));
            }
        }else {
            api.log("Trees have a lot of players around, consider using another world");
            tree = api.getObjects().getAll().stream().filter(secondBestTree).collect(Collectors.toList());
            tree.sort(Comparator.<RS2Object>comparingInt(this::peopleAroundEntity)
                    .thenComparingInt(this::peopleAroundEntity));
            NodeWoodCutter.currentTree = tree.get(0);
            if (tree.get(0).interact("Chop down")){
                Timing.waitCondition(() -> NodeWoodCutter.gainedExp != api.getExperienceTracker().getGainedXP(Skill.WOODCUTTING)
                        , api.random(900, 1200));
                NodeWoodCutter.state = "Chopping Tree";
            }

        }

    }

    private int peopleAroundEntity(Entity e){
        return toIntExact(api.players.getAll().stream().filter(x -> e.getArea(2).contains(x)).count());
    }

}