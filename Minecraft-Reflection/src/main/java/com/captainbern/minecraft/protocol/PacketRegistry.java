package com.captainbern.minecraft.protocol;

import static com.captainbern.reflection.matcher.Matchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.ClassTemplate;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.captainbern.reflection.SafeMethod;
import com.captainbern.reflection.accessor.FieldAccessor;
import com.captainbern.reflection.accessor.MethodAccessor;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PacketRegistry {

    private BiMap<PacketType, Class<?>> typeToClass = HashBiMap.create();
    private Set<PacketType> serverPackets = Sets.newHashSet();
    private Set<PacketType> clientPackets = Sets.newHashSet();

    public PacketRegistry() {
        registerPackets();
    }

    public Map<PacketType, Class<?>> getClassLookup() {
        return Collections.unmodifiableMap(typeToClass);
    }

    public Map<Class<?>, PacketType> getTypeLookup() {
        return Collections.unmodifiableMap(typeToClass.inverse());
    }

    public Set<PacketType> getServerPackets() {
        return Collections.unmodifiableSet(serverPackets);
    }

    public Set<PacketType> getClientPackets() {
        return Collections.unmodifiableSet(clientPackets);
    }

	@SuppressWarnings({"unchecked", "rawtypes"})
	protected void registerPackets(){
		Object[] protocolTypes = MinecraftReflection.getEnumProtocolClass().getEnumConstants();
        List<Map<Integer, Class<?>>> clientPackets = Lists.newArrayList();
        List<Map<Integer, Class<?>>> serverPackets = Lists.newArrayList();

        Reflection reflection = new Reflection();
        ClassTemplate enumProtocol = reflection.reflect(MinecraftReflection.getEnumProtocolClass());
        ClassTemplate enumProtocolDirection = null;
        try {
            enumProtocolDirection = reflection.reflect(MinecraftReflection.getMinecraftClass("EnumProtocolDirection"));
        } catch (RuntimeException e) {
            if (!ClassNotFoundException.class.isAssignableFrom(e.getCause().getClass())) {
                throw e;
            } // else: running an older server version in which this class is not present
        }

        // Check if the direction enum is required for map retrieval (there's probably a better way to do this...)
        if (enumProtocolDirection != null) {
            FieldAccessor<Map> packets = ((SafeField<Map>) enumProtocol.getSafeFields(withExactType(Map.class)).get(1)).getAccessor();
            Object[] protocolDirections = enumProtocolDirection.getReflectedClass().getEnumConstants();

            // EnumProtocolDirection -> Map<Integer, Packet.class>
            for(Object protocolType : protocolTypes) {
                Map<?, ?> packetMap = packets.get(protocolType);
                Map<Integer, Class<?>> inboundPackets =  (Map<Integer, Class<?>>) packetMap.get(protocolDirections[0]);
                Map<Integer, Class<?>> outboundPackets =  (Map<Integer, Class<?>>) packetMap.get(protocolDirections[1]);
                clientPackets.add(inboundPackets == null ? (BiMap) HashBiMap.create() : inboundPackets);
                serverPackets.add(outboundPackets == null ? (BiMap) HashBiMap.create() : outboundPackets);
            }
        } else {
            MethodAccessor<Map<Integer, Class<?>>> inboundPackets = (MethodAccessor<Map<Integer, Class<?>>>) ((SafeMethod) enumProtocol.getSafeMethods(withReturnType(Map.class), withArgumentCount(0)).get(0)).getAccessor();
            MethodAccessor<Map<Integer, Class<?>>> outboundPackets = (MethodAccessor<Map<Integer, Class<?>>>) ((SafeMethod) enumProtocol.getSafeMethods(withReturnType(Map.class), withArgumentCount(0)).get(1)).getAccessor();

            for(Object protocolType : protocolTypes) {
                clientPackets.add(inboundPackets.invoke(protocolType)); // client packets
                serverPackets.add(outboundPackets.invoke(protocolType)); // server packets
            }
        }

        // If there are more client packets than server packets, then we messed up the fields.
        if (sum(clientPackets) > sum(serverPackets)) {
            List<Map<Integer, Class<?>>> temp = serverPackets;
            serverPackets = clientPackets;
            clientPackets = temp;
        }

        for(int i = 0; i < protocolTypes.length; i++) {
            Enum<?> protocolType = (Enum<?>) protocolTypes[i];
            setPackets(clientPackets.get(i), Protocol.fromVanilla(protocolType), Sender.CLIENT);
            setPackets(serverPackets.get(i), Protocol.fromVanilla(protocolType), Sender.SERVER);
        }
    }

    protected void setPackets(Map<Integer, Class<?>> map, Protocol protocol, Sender sender) {
        for(Map.Entry<Integer, Class<?>> entry : map.entrySet()) {
			String packetClassName = entry.getValue().getSimpleName();
			PacketType type = PacketType.getTypeFrom(protocol, sender, packetClassName);

            typeToClass.put(type, entry.getValue());
            if(sender == Sender.CLIENT) {
                clientPackets.add(type);
            }

            if(sender == Sender.SERVER) {
                serverPackets.add(type);
            }
        }
    }

    private int sum(Iterable<? extends Map<Integer, Class<?>>> maps) {
        int count = 0;

        for (Map<Integer, Class<?>> map : maps)
            count += map.size();
        return count;
    }
}
