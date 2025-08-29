package dev.wuason.mechanics.actions.args.def.internal.functions;

import dev.wuason.mechanics.actions.args.def.internal.functions.def.RandomObject;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.adapter.GetAdapterId;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.adapter.GetItemStackByAdapter;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.math.Chance;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.math.RandomNumber;

import java.util.HashMap;
import java.util.function.Consumer;

public class FunctionsInternal {
    public static final HashMap<String, FunctionInternal> FUNCTIONS = new HashMap<>();


    static {

        //************ MATH ************//

        FunctionInternal chance = new Chance();
        FUNCTIONS.put(chance.getId(), chance);

        FunctionInternal randomNumber = new RandomNumber();
        FUNCTIONS.put(randomNumber.getId(), randomNumber);

        //************ OBJECT ************//

        FunctionInternal randomObject = new RandomObject();
        FUNCTIONS.put(randomObject.getId(), randomObject);

        //************ ADAPTER ************//

        FunctionInternal getAdapterId = new GetAdapterId();
        FUNCTIONS.put(getAdapterId.getId(), getAdapterId);
        FunctionInternal getItemStackByAdapter = new GetItemStackByAdapter();
        FUNCTIONS.put(getItemStackByAdapter.getId(), getItemStackByAdapter);


    }

    public static void register(Consumer<FunctionInternal.Builder> builderConsumer){
        FunctionInternal.Builder builder = new FunctionInternal.Builder();
        builderConsumer.accept(builder);
        FunctionInternal functionInternal = builder.build();
        FUNCTIONS.put(functionInternal.getId(), functionInternal);

    }
}
