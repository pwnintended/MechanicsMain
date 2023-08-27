package me.deecaad.weaponmechanics.weapon.placeholders;

import me.deecaad.core.placeholder.PlaceholderData;
import me.deecaad.core.placeholder.PlaceholderHandler;
import me.deecaad.weaponmechanics.utils.CustomTag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PAmmoLeft extends PlaceholderHandler {

    public PAmmoLeft() {
        super("ammo_left");
    }

    @Nullable
    @Override
    public String onRequest(@NotNull PlaceholderData data) {
        if (data.item() == null) return null;

        return String.valueOf(CustomTag.AMMO_LEFT.getInteger(data.item()));
    }
}
