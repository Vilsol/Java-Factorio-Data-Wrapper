package com.demod.factorio;

import com.demod.factorio.prototype.DataPrototype;
import org.json.JSONObject;
import org.luaj.vm2.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonExtractor {

    public static void main(String[] args) throws IOException{
        Files.write(Paths.get("output.json"), dataTableToJson(FactorioData.getTable()).toString().getBytes());
    }

    private static JSONObject dataTableToJson(DataTable table){
        JSONObject result = new JSONObject();
        result.put("entities", prototypesToJson(table.getEntities()));
        result.put("items", prototypesToJson(table.getItems()));
        result.put("recipes", prototypesToJson(table.getRecipes()));
        result.put("expensiveRecipes", prototypesToJson(table.getExpensiveRecipes()));
        result.put("fluids", prototypesToJson(table.getFluids()));
        result.put("technologies", prototypesToJson(table.getTechnologies()));
        result.put("equipments", prototypesToJson(table.getEquipments()));
        result.put("tiles", prototypesToJson(table.getTiles()));
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
