import org.osbot.rs07.script.MethodProvider;

public class WalkToTrees extends Task {

    public WalkToTrees(MethodProvider api) {
        super(api);
    }

    @Override
    public boolean canProcess() {

        return !api.inventory.isFull() && !NodeWoodCutter.WOOD_AREA.contains(api.myPlayer());

    }

    @Override
    public void process() {

        NodeWoodCutter.state = "Walking to trees";

        api.getWalking().webWalk(NodeWoodCutter.WOOD_AREA);

    }

}