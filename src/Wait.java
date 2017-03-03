import org.osbot.rs07.script.MethodProvider;

public class Wait extends Task {

    public Wait(MethodProvider api) {
        super(api);
    }

    @Override
    public boolean canProcess() {
        return api.myPlayer().isAnimating() && NodeWoodCutter.currentTree.exists();
    }

    @Override
    public void process() {

        NodeWoodCutter.state = " Waiting....";

        Timing.waitCondition(() -> !this.canProcess(), 5000);

    }

}