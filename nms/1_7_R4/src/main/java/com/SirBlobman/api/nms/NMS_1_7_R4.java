package com.SirBlobman.api.nms;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;
import com.SirBlobman.api.nms.boss.bar.BossBarHandler_1_7_R4;

public class NMS_1_7_R4 extends NMS_Handler {
    private final BossBarHandler bossBarHandler = new BossBarHandler_1_7_R4();
    private final PlayerHandler playerHandler = new PlayerHandler_1_7_R4();
    private final EntityHandler entityHandler = new EntityHandler_1_7_R4();
    
    @Override
    public BossBarHandler getBossBarHandler() {
        return this.bossBarHandler;
    }
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
    
    @Override
    public EntityHandler getEntityHandler() {
        return this.entityHandler;
    }
}