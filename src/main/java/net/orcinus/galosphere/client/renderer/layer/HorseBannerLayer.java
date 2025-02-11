package net.orcinus.galosphere.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.orcinus.galosphere.api.BannerAttachable;
import net.orcinus.galosphere.init.GItems;
import net.orcinus.galosphere.mixin.access.AbstractHorseAccessor;

@Environment(EnvType.CLIENT)
public class HorseBannerLayer extends RenderLayer<Horse, HorseModel<Horse>> {

    public HorseBannerLayer(RenderLayerParent<Horse, HorseModel<Horse>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, Horse entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        if (entity.getArmor().is(GItems.STERLING_HORSE_ARMOR)) {
            if (!((BannerAttachable) entity).getBanner().isEmpty()) {
                ItemStack itemstack = ((BannerAttachable) entity).getBanner();
                if (itemstack != null) {
                    if (!itemstack.isEmpty()) {
                        Item item = itemstack.getItem();
                        poseStack.pushPose();
                        poseStack.scale(1.0F, 1.0F, 1.0F);
                        if (!(item instanceof ArmorItem) || ((ArmorItem) item).getEquipmentSlot() != EquipmentSlot.HEAD) {
                            poseStack.translate(0.0D, 0.35D, 0.3D);
                            if (((AbstractHorseAccessor)entity).getStandAnimO() > 0.0F) {
                                poseStack.translate(0.0D, 0.35D, 0.35D);
                            }
                            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                            poseStack.scale(0.625F, -0.625F, -0.625F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemstack, ItemDisplayContext.HEAD, false, poseStack, source, entity.level(), packedLight, OverlayTexture.NO_OVERLAY, entity.getId() + ItemDisplayContext.HEAD.ordinal());
                        }
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}
