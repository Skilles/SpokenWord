package com.skilles.spokenword.config;

import com.skilles.spokenword.util.ConfigUtil;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.client.gui.Element;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.skilles.spokenword.SpokenWord.log;
import static com.skilles.spokenword.util.ConfigUtil.*;

/**
 * Custom implementation of selection list
 */
public class EntitySelectorCreator extends DropdownBoxEntry.DefaultSelectionCellCreator<String> {
    ListModes mode;
    public EntitySelectorCreator(ListModes mode) {
        super();
        this.mode = mode;
    }
    @Override
    public DropdownBoxEntry.SelectionCellElement<String> create(String selection) {
            return new EntitySelectionElement<java.lang.String>(selection, new Function<String, Text>() {
                @Override
                public Text apply(String s) {
                    if (containsEntity(s, mode)) return new LiteralText(s).formatted(Formatting.STRIKETHROUGH);
                    return new LiteralText(s);
                }
            }, mode);
    }
}
class EntitySelectionElement<R> extends DropdownBoxEntry.DefaultSelectionCellElement<R> {
    ListModes mode;
    public EntitySelectionElement(R r, Function<R, Text> toTextFunction, ListModes mode) {
        super(r, toTextFunction);
        this.mode = mode;
    }

    /**
     * To always display suggestions TODO: remove
     */
    @Override
    public @Nullable Text getSearchKey() {
        return null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int int_1) {
        boolean b = this.rendering && this.toTextFunction.apply(r).getStyle().equals(Style.EMPTY) && mouseX >= (double)this.x && mouseX <= (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY <= (double)(this.y + this.height);
        if (b) {
            List<String> prevList = Collections.unmodifiableList(modeToTemp(mode));
            addToList((String) r, modeToTemp(mode));
            this.getEntry().getSelectionElement().getTopRenderer().setValue((R) listToString(modeToTemp(mode)));
            setTempList(stringToList((String) this.getEntry().getSelectionElement().getTopRenderer().getValue()), mode);
            return true;
        } else {
            return false;
        }
    }
}
