package net.lapointep.paulsmod.util;

import com.mojang.serialization.Codec;
import net.lapointep.paulsmod.PaulsMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.Map;

public class BankSavedData extends SavedData {
    public static final String STORAGE_ID = "paulsmods_bank_accounts";

    public static final SavedDataType<BankSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(PaulsMod.MOD_ID, STORAGE_ID),
            BankSavedData::new,
            Codec.PASSTHROUGH.xmap(
                    dynamic -> {
                        BankSavedData data = new BankSavedData();
                        if (dynamic.getValue() instanceof CompoundTag nbt) {
                            data.load(nbt);
                        }
                        return data;
                    },
                    data -> {
                        CompoundTag nbt = new CompoundTag();
                        data.save(nbt);
                        return new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, nbt);
                    }
            ),
            DataFixTypes.SAVED_DATA_COMMAND_STORAGE // Best fit for generic mod data
    );

    public BankSavedData() {
    }

    public static BankSavedData get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
        }
        return new BankSavedData();
    }

    public void load(CompoundTag nbt) {
        if (nbt.contains("accounts")) {
            ListTag accountsList = nbt.getListOrEmpty("accounts");
            PaulsMod.bankAccounts.clear();
            for (int i = 0; i < accountsList.size(); i++) {
                CompoundTag accountNbt = accountsList.getCompoundOrEmpty(i);
                String owner = accountNbt.getStringOr("owner", "");
                int balance = accountNbt.getIntOr("balance", 0);
                if (!owner.isEmpty()) {
                    PaulsMod.bankAccounts.put(owner, new BankAccount(owner, balance));
                }
            }
        }
    }

    public void save(CompoundTag nbt) {
        ListTag accountsList = new ListTag();
        for (Map.Entry<String, BankAccount> entry : PaulsMod.bankAccounts.entrySet()) {
            CompoundTag accountNbt = new CompoundTag();
            accountNbt.putString("owner", entry.getValue().getOwner());
            accountNbt.putInt("balance", entry.getValue().getBalance());
            accountsList.add(accountNbt);
        }
        nbt.put("accounts", accountsList);
    }
}
