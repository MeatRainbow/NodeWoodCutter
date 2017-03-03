import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BankItems extends Task {

    public BankItems(MethodProvider api) {
        super(api);
    }

    private Predicate<RS2Object> bankFilter = bank ->
            bank != null &&
                    bank.getName().equalsIgnoreCase("bank booth") &&
                    Banks.VARROCK_WEST.contains(bank);

    @Override
    public boolean canProcess() {

        return Banks.VARROCK_WEST.contains(api.myPlayer()) && api.inventory.isFull();

    }

    @Override
    public void process() {
        NodeWoodCutter.state = "State: Banking ";
        List<RS2Object> banks = api.getObjects().getAll().stream().filter(bankFilter).collect(Collectors.toList());
        api.log("Found " + banks.size() + " suitable booths! " );

        int chosenBooth = api.random(0, banks.size()-1);

        if (banks.size() > 0){
            banks.get(chosenBooth).interact("Bank");
            Timing.waitCondition(() -> api.getBank().isOpen(), 5000);
            if (api.getBank().isOpen()){
                api.getBank().depositAllExcept(item -> item.getName().endsWith("axe"));
            }
        }else {
            api.log("Invalid booth! ");

        }






    }





}