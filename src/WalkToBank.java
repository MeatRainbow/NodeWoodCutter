import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.script.MethodProvider;

public class WalkToBank extends Task {

    public WalkToBank(MethodProvider api) {
        super(api);
    }

    @Override
    public boolean canProcess() {

        return api.inventory.isFull() && !Banks.VARROCK_WEST.contains(api.myPlayer());

    }

    @Override
    public void process() {
        NodeWoodCutter.state = "State: Walking to bank";
        api.getWalking().webWalk(Banks.VARROCK_WEST);

    }

}