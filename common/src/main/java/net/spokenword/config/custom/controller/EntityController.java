package net.spokenword.config.custom.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl3.api.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;

import java.util.function.Function;
import java.util.function.Predicate;

public class EntityController extends AbstractRegistryController<EntityType<?>>
{
    private final boolean hideHostiles;

    private final boolean hidePassives;

    public EntityController(Option<EntityType<?>> option, boolean hideHostiles, boolean hidePassives)
    {
        super(option);
        this.hideHostiles = hideHostiles;
        this.hidePassives = hidePassives;
    }

    @Override
    protected DefaultedRegistry<EntityType<?>> getRegistry()
    {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    @Override
    protected Function<EntityType<?>, String> alternativeKey()
    {
        return EntityType::toShortString;
    }

    @Override
    protected Function<EntityType<?>, Component> displayFormatter()
    {
        return EntityType::getDescription;
    }

    @Override
    protected Predicate<EntityType<?>> optionFilter()
    {
        return (type) ->
        {
            var category = type.getCategory();

            if (category == MobCategory.MISC && !type.equals(EntityType.VILLAGER))
            {
                return false;
            }
            if (hideHostiles && !category.isFriendly())
            {
                return false;
            }

            if (hidePassives && category.isFriendly())
            {
                return false;
            }
            return true;
        };
    }

    @Override
    protected void renderValueEntry(GuiGraphics graphics, EntityType<?> entityType, int x, int y, float delta)
    {
        var level = Minecraft.getInstance().level;

        if (level == null)
            return;

        var tempEntity = entityType.create(level);

        // TODO: Fix this
        if (tempEntity != null)
        {
            var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(tempEntity);

            // Prepare the render
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate(x, y, 100); // Adjust positioning
            //poseStack.scale(scale, scale, scale); // Adjust scaling

            // Render the head part of the model
            // This is a generic call, you need to adapt it to your entity model
            //model.renderToBuffer(poseStack, graphics.bufferSource().getBuffer(RenderType.entityCutout(texture)), 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            renderer.render(tempEntity, 0, 0, poseStack, graphics.bufferSource(), 0xF000F0);

            graphics.flush();

            poseStack.popPose();
        }
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, ResourceLocation identifier, int x, int y)
    {
        renderValueEntry(graphics, getRegistry().get(identifier), x, y, 0);
    }

}
