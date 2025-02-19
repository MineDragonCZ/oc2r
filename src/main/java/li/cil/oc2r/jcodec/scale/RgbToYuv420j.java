/* SPDX-License-Identifier: BSD 2-Clause "Simplified" License */

package li.cil.oc2r.jcodec.scale;

import li.cil.oc2r.jcodec.common.model.Picture;
import li.cil.oc2r.jcodec.common.tools.MathUtil;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * <p>
 * Transforms Picture in RGB colorspace ( one plane, 3 integers per pixel ) to
 * Yuv420 colorspace output picture ( 3 planes, luma - 0th plane, cb - 1th
 * plane, cr - 2nd plane; cb and cr planes are half width and half haight )
 * <p>
 * TODO: implement jpeg colorspace instead of NTSC
 *
 * @author The JCodec project
 */
public final class RgbToYuv420j implements Transform {
    public RgbToYuv420j() {
    }

    @Override
    public void transform(final Picture img, final Picture dst) {
        final byte[] y = img.getData()[0];
        final byte[][] dstData = dst.getData();
        final int[][] out = new int[4][3];

        int offChr = 0;
        int offLuma = 0;
        int offSrc = 0;
        final int strideSrc = img.getWidth() * 3;
        final int strideDst = dst.getWidth();
        for (int i = 0; i < img.getHeight() >> 1; i++) {
            for (int j = 0; j < img.getWidth() >> 1; j++) {
                dstData[1][offChr] = 0;
                dstData[2][offChr] = 0;

                rgb2yuv(y[offSrc], y[offSrc + 1], y[offSrc + 2], out[0]);
                dstData[0][offLuma] = (byte) out[0][0];

                rgb2yuv(y[offSrc + strideSrc], y[offSrc + strideSrc + 1], y[offSrc + strideSrc + 2], out[1]);
                dstData[0][offLuma + strideDst] = (byte) out[1][0];

                ++offLuma;

                rgb2yuv(y[offSrc + 3], y[offSrc + 4], y[offSrc + 5], out[2]);
                dstData[0][offLuma] = (byte) out[2][0];

                rgb2yuv(y[offSrc + strideSrc + 3], y[offSrc + strideSrc + 4], y[offSrc + strideSrc + 5], out[3]);
                dstData[0][offLuma + strideDst] = (byte) out[3][0];
                ++offLuma;

                dstData[1][offChr] = (byte) ((out[0][1] + out[1][1] + out[2][1] + out[3][1] + 2) >> 2);
                dstData[2][offChr] = (byte) ((out[0][2] + out[1][2] + out[2][2] + out[3][2] + 2) >> 2);

                ++offChr;
                offSrc += 6;
            }
            offLuma += strideDst;
            offSrc += strideSrc;
        }
    }

    public static void rgb2yuv(final byte r, final byte g, final byte b, final int[] out) {
        final int rS = r + 128;
        final int gS = g + 128;
        final int bS = b + 128;
        int y = 77 * rS + 150 * gS + 15 * bS;
        int u = -43 * rS - 85 * gS + 128 * bS;
        int v = 128 * rS - 107 * gS - 21 * bS;
        y = (y + 128) >> 8;
        u = (u + 128) >> 8;
        v = (v + 128) >> 8;

        out[0] = MathUtil.clip(y - 128, -128, 127);
        out[1] = MathUtil.clip(u, -128, 127);
        out[2] = MathUtil.clip(v, -128, 127);
    }
}
