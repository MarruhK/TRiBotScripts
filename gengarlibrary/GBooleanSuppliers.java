package scripts.gengarlibrary;

import org.tribot.api.General;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import java.util.function.BooleanSupplier;

public class GBooleanSuppliers
{
    @Deprecated
    public static Condition conditionIsAnimation(int animation)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Player.getAnimation() == animation;
            }
        };
    }

    @Deprecated
    public static Condition conditionIsAnimationNot(int animation)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Player.getAnimation() != animation;
            }
        };
    }

    @Deprecated
    public static Condition conditionIsUptext(String uptext)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Game.isUptext(uptext);
            }
        };
    }

    @Deprecated
    public static Condition conditionIsInChatDialogue()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(231) != null || Interfaces.get(217) != null;
            }
        };
    }

    @Deprecated
    public static Condition conditionWaitToStopMoving()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return !Player.isMoving();
            }
        };
    }

    @Deprecated
    public static Condition conditionIsGameTabOpen(GameTab.TABS tab)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return GameTab.getOpen() == tab;
            }
        };
    }

    @Deprecated
    public static Condition conditionIsInterfaceValid(int masterIndex)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(masterIndex) != null;
            }
        };
    }

    @Deprecated
    public static Condition conditionIsInterfaceNotValid(int masterIndex)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(masterIndex) == null;
            }
        };
    }

    public static BooleanSupplier isInterfaceValid(int masterIndex)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.isInterfaceValid(masterIndex);
            }
        };
    }

    public static BooleanSupplier isInterfaceNotValid(int masterIndex)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return !Interfaces.isInterfaceValid(masterIndex);
            }
        };
    }

    public static BooleanSupplier waitToStopMoving()
    {
        General.sleep(125, 200);
        return ()-> !Player.isMoving();
    }

    public static BooleanSupplier waitToStartMoving()
    {
        General.sleep(125, 200);
        return ()-> Player.isMoving();
    }

    public static BooleanSupplier waitForAnimation(int animation)
    {
        General.sleep(125, 200);
        return ()-> Player.getAnimation() == animation;
    }

    public static BooleanSupplier isInArea(RSArea area)
    {
        General.sleep(125, 200);
        return ()-> area.contains(Player.getPosition());
    }

    public static BooleanSupplier isInOneAreas(RSArea... areas)
    {
        General.sleep(125, 200);
        RSTile pos = Player.getPosition();

        for (RSArea area : areas)
        {
            if (area.contains(pos))
            {
                return ()-> true;
            }
        }

        return ()-> false;
    }

    public static BooleanSupplier isNotInAreas(RSArea... areas)
    {
        General.sleep(125, 200);
        RSTile pos = Player.getPosition();
        boolean isNotInArea = true;

        for (RSArea area : areas)
        {
            if (area.contains(pos))
            {
                isNotInArea = false;
            }
        }

        boolean finalIsNotInArea = isNotInArea;
        return ()-> finalIsNotInArea;
    }

    public static BooleanSupplier isNpcOptionValid()
    {
        General.sleep(125, 200);
        return ()-> NPCChat.getOptions() != null;
    }

    public static BooleanSupplier isNpcOptionNotValid()
    {
        General.sleep(125, 200);
        return ()-> NPCChat.getOptions() == null;
    }

    public static BooleanSupplier isNpcContinueNull()
    {
        General.sleep(125, 200);
        return ()-> NPCChat.getClickContinueInterface() == null;
    }

    public static BooleanSupplier isPotionSipped(int currentLvl, Skills.SKILLS skill)
    {
        General.sleep(125, 200);
        return ()-> currentLvl <= Skills.getCurrentLevel(skill);
    }

    public static BooleanSupplier isUptext(String uptext)
    {
        General.sleep(125, 200);
        return ()-> Game.isUptext(uptext);
    }

    public static BooleanSupplier isItemWithdrawn(int id)
    {
        return isItemWithdrawn(1, id);
    }

    public static BooleanSupplier isItemWithdrawn(int count, int id)
    {
        General.sleep(125, 200);
        return ()-> Inventory.find(id).length >= count;
    }

    public static BooleanSupplier isTradeScreenState(Trading.WINDOW_STATE state)
    {
        General.sleep(1000);
        return ()-> Trading.getWindowState() == state;
    }

    public static BooleanSupplier isItemInInventory(int... ids)
    {
        General.sleep(200);
        return ()-> Inventory.find(ids).length > 0;
    }
}
