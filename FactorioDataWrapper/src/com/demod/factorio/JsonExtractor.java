package com.demod.factorio;

import com.demod.factorio.prototype.DataPrototype;
import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.luaj.vm2.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonExtractor {

    public static void main(String[] args) throws IOException{
        Files.write(Paths.get("output.json"), dataTableToJson(FactorioData.getTable(), Lists.newArrayList(args)).toString().getBytes());
    }

    private static JSONObject dataTableToJson(DataTable table, List<String> args){
        JSONObject result = new JSONObject();

        boolean noRendering = args.contains("--no-rendering");
        boolean noSound = args.contains("--no-sound");
        boolean noAutoplace = args.contains("--no-autoplace");

        if(!args.contains("--no-entities")){
            Map<String, JSONObject> entities = prototypesToJson(table.getEntities());

            if(noRendering || noSound || noAutoplace){
                entities.values().forEach(entity -> {
                    if(noRendering){
                        for(String k : entity.keySet().toArray(new String[entity.length()])){
                            if(k.contains("animation")
                                    || k.contains("animations")
                                    || k.endsWith("_sprite")
                                    || k.endsWith("_sprites")
                                    || k.endsWith("_box")
                                    || k.endsWith("_boxes")
                                    || k.endsWith("_light")
                                    || k.endsWith("_lights")
                                    || k.endsWith("_signal")
                                    || k.endsWith("_color")
                                    || k.endsWith("_picture")
                                    || k.endsWith("_pictures")
                                    || k.endsWith("connection_point")
                                    || k.endsWith("connection_points")
                                    || k.endsWith("_side")
                                    || k.endsWith("_top")
                                    || k.endsWith("_bottom")
                                    || k.endsWith("_vertical")
                                    || k.endsWith("_horizontal")
                                    || k.endsWith("_effect")
                                    || k.endsWith("_effects")
                                    || k.endsWith("_shadow")
                                    || k.endsWith("_shadows")
                                    || k.endsWith("_mask")
                                    || k.endsWith("_masks")
                                    || k.endsWith("_tint")
                                    || k.endsWith("_offset")
                                    || k.endsWith("_offsets")
                                    || k.startsWith("gui_")
                                    || k.startsWith("horizontal_")
                                    || k.startsWith("vertical_")){
                                entity.remove(k);
                            }
                        }

                        entity.remove("colors");
                        entity.remove("variations");
                        entity.remove("action");
                        entity.remove("animation");
                        entity.remove("animations");
                        entity.remove("rail_piece");
                        entity.remove("picture");
                        entity.remove("pictures");
                        entity.remove("minable");
                        entity.remove("color");
                        entity.remove("light1");
                        entity.remove("light2");
                        entity.remove("sprite");
                        entity.remove("sprites");
                        entity.remove("shadow");
                        entity.remove("shadows");
                        entity.remove("connection_patches");
                        entity.remove("connection_patches_connected");
                        entity.remove("connection_patches_disconnected");
                        entity.remove("light");
                        entity.remove("ending_patch");
                        entity.remove("structure");
                        entity.remove("splash");
                        entity.remove("smoke");
                        entity.remove("final_action");
                        entity.remove("in_motion");
                        entity.remove("idle");
                        entity.remove("destroy_action");
                        entity.remove("shadow_idle");
                        entity.remove("shadow_in_motion");
                        entity.remove("picture_off");
                        entity.remove("light_when_colored");
                        entity.remove("picture_on");
                        entity.remove("signal_to_color_mapping");
                        entity.remove("fluid_wagon_connector_graphics");
                        entity.remove("screen_light_offsets");
                        entity.remove("working_visualisations");
                        entity.remove("wheels");
                        entity.remove("working_visualisations_disabled");
                        entity.remove("ground_patch");
                        entity.remove("ground_patch_higher");
                        entity.remove("head");
                        entity.remove("body");
                        entity.remove("tail");
                        entity.remove("smoke_sources");
                        entity.remove("particle");
                        entity.remove("idle_with_cargo");
                        entity.remove("shadow_in_motion_with_cargo");
                        entity.remove("shadow_idle_with_cargo");
                        entity.remove("in_motion_with_cargo");
                        entity.remove("pipe_covers");
                        entity.remove("vertical_base");
                        entity.remove("horizontal_rail_base");
                        entity.remove("vertical_rail_base");
                        entity.remove("horizontal_rail_animation_left");
                        entity.remove("vertical_rail_animation_right");
                        entity.remove("horizontal_rail_animation_right");
                        entity.remove("vertical_rail_animation_left");
                        entity.remove("wall_patch");
                        entity.remove("horizontal_base");
                        entity.remove("corpse");
                        entity.remove("working");
                        entity.remove("sparks");
                        entity.remove("shadow_working");
                        entity.remove("fire");
                        entity.remove("fire_glow");
                    }

                    if(noSound){
                        for(String k : entity.keySet().toArray(new String[entity.length()])){
                            if(k.contains("sound") || k.endsWith("_trigger")){
                                entity.remove(k);
                            }
                        }
                    }

                    if(noAutoplace){
                        entity.remove("autoplace");
                    }
                });
            }

            result.put("entities", entities);
        }

        if(!args.contains("--no-items")){
            result.put("items", prototypesToJson(table.getItems()));
        }

        if(!args.contains("--no-recipes")){
            result.put("recipes", prototypesToJson(table.getRecipes()));
        }

        if(!args.contains("--no-expensive-recipes")){
            result.put("expensiveRecipes", prototypesToJson(table.getExpensiveRecipes()));
        }

        if(!args.contains("--no-fluids")){
            result.put("fluids", prototypesToJson(table.getFluids()));
        }

        if(!args.contains("--no-technologies")){
            result.put("technologies", prototypesToJson(table.getTechnologies()));
        }

        if(!args.contains("--no-equipments")){
            result.put("equipments", prototypesToJson(table.getEquipments()));
        }

        if(!args.contains("--no-tiles")){
            result.put("tiles", prototypesToJson(table.getTiles()));
        }

        return result;
    }

    private static Map<String, JSONObject> prototypesToJson(Map<String, ? extends DataPrototype> prototypes){
        return prototypes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> tableToJson(e.getValue().lua())));
    }

    private static JSONObject tableToJson(LuaTable table){
        JSONObject result = new JSONObject();
        for(LuaValue key : table.keys()){
            LuaValue val = table.get(key);
            if(val instanceof LuaTable) {
                result.put(key.tojstring(), tableToJson((LuaTable) val));
            }else if(val instanceof LuaBoolean){
                result.put(key.tojstring(), val.toboolean());
            }else if(val instanceof LuaDouble){
                result.put(key.tojstring(), val.todouble());
            }else if(val instanceof LuaInteger){
                result.put(key.tojstring(), val.toint());
            }else if(val instanceof LuaNumber){
                result.put(key.tojstring(), val.todouble());
            }else{
                result.put(key.tojstring(), val.tojstring());
            }
        }
        return result;
    }

}
