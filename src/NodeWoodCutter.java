import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@ScriptManifest(author = "LRDBLK", info = "cuts oaks ", name = "NodeCutter", version = 1, logo = "")
public class NodeWoodCutter extends Script {

    final static Area WOOD_AREA = new Area(3157, 3418, 3173, 3369);
    static int gainedExp = 0;
    static int gainedLogs = 0;
    static String state = "";
    private ArrayList<Task> taskList = new ArrayList<>();
    static RS2Object currentTree = null;

    private final Color color1 = new Color(0, 0, 0);
    private final Color color2 = new Color(255, 153, 102);
    private final Color color3 = new Color(153, 0, 0);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 17);


    @Override
    public void onStart() {
        log("Let's get started!");
        getExperienceTracker().start(Skill.WOODCUTTING);
        taskList.add(new ChopWood(this));
        taskList.add(new WalkToBank(this));
        taskList.add(new WalkToTrees(this));
        taskList.add(new BankItems(this));
        taskList.add(new Wait(this));
    }

    @Override
    public int onLoop() throws InterruptedException {

        try{
            taskList.stream().filter(x -> x.canProcess()).findFirst().get().process();
        }catch (NoSuchElementException e){
            CustomTiming.waitCondition(() -> !myPlayer().isAnimating(), random(1000, 2000));
        }


        return random(200, 600);
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        String text = message.getMessage().toLowerCase();

        if (text.contains("some oak logs")){
            gainedLogs++;
        }
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onPaint(Graphics2D g) {

        Point mP = getMouse().getPosition();
        g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
        g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);
        g.setColor(Color.white);

        g.setColor(color1);
        g.fillRect(217, 347, 296, 114);
        g.setStroke(stroke1);
        g.drawRect(217, 347, 296, 114);
        g.setColor(color2);
        g.fillRect(214, 344, 296, 114);
        g.setColor(color3);
        g.drawRect(214, 344, 296, 114);
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("State: " + state, 220, 365);
        g.drawString("Runtime: " + CustomTiming.formatMS(getExperienceTracker().getElapsed(Skill.WOODCUTTING)), 220, 386);
        g.drawString("Logs Collected: " + gainedLogs, 219, 405);
        g.drawString("EXP/hour: " + getExperienceTracker().getGainedXPPerHour(Skill.WOODCUTTING), 220, 427);
        g.drawString("Levels gained (current level): " + getExperienceTracker().getGainedLevels(Skill.WOODCUTTING)
                +" (" + skills.getStatic(Skill.WOODCUTTING) +")", 220, 449);


    }

}