package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder
{
    private final byte[] buffer = new byte[8192];
    private final Deflater deflater;
    private int treshold;
    private static final String __OBFID = "CL_00002313";

    public NettyCompressionEncoder(int treshold)
    {
        this.treshold = treshold;
        this.deflater = new Deflater();
    }

    protected void compress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output)
    {
        int i = input.readableBytes();
        PacketBuffer packetbuffer = new PacketBuffer(output);

        if (i < this.treshold)
        {
            packetbuffer.writeVarIntToBuffer(0);
            packetbuffer.writeBytes(input);
        }
        else
        {
            byte[] abyte = new byte[i];
            input.readBytes(abyte);
            packetbuffer.writeVarIntToBuffer(abyte.length);
            this.deflater.setInput(abyte, 0, i);
            this.deflater.finish();

            while (!this.deflater.finished())
            {
                int j = this.deflater.deflate(this.buffer);
                packetbuffer.writeBytes(this.buffer, 0, j);
            }

            this.deflater.reset();
        }
    }

    public void setCompressionTreshold(int treshold)
    {
        this.treshold = treshold;
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_)
    {
        this.compress(p_encode_1_, (ByteBuf)p_encode_2_, p_encode_3_);
    }
}