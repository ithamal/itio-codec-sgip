package io.github.ithmal.itio.codec.sgip.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: ken.lin
 * @since: 2023-10-01 09:57
 */
@Getter
public abstract class SgipMessage {

    @Setter
    protected long sequenceId;

    private final Command command;

    public static final short VERSION_30 = 0x30;

    public static final short VERSION_20 = 0x20;

    public SgipMessage(Command command, long sequenceId) {
        this.command = command;
        this.sequenceId = sequenceId;
    }
}
