package io.github.ithmal.itio.codec.sgip.message;


import io.github.ithmal.itio.codec.sgip.base.Command;
import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:32
 */
@Getter
@Setter
public class UnBindResponse extends SgipMessage {

    public UnBindResponse(long sequenceId) {
        super(Command.UNBIND_RESPONSE, sequenceId);
    }

    @Override
    public int getLength() {
        return 0;
    }
}

