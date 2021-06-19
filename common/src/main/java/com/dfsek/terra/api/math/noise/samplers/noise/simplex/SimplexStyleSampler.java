package com.dfsek.terra.api.math.noise.samplers.noise.simplex;

import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;

/**
 * Abstract NoiseSampler implementation for simplex-style noise functions.
 */
public abstract class SimplexStyleSampler extends NoiseFunction {
    protected static final double[] GRADIENTS_2_D = {
            0.130526192220052d, 0.99144486137381d, 0.38268343236509d, 0.923879532511287d, 0.608761429008721d, 0.793353340291235d,
            0.793353340291235d, 0.608761429008721d, 0.923879532511287d, 0.38268343236509d, 0.99144486137381d, 0.130526192220051d,
            0.99144486137381d, -0.130526192220051d, 0.923879532511287d, -0.38268343236509d, 0.793353340291235d, -0.60876142900872d,
            0.608761429008721d, -0.793353340291235d, 0.38268343236509d, -0.923879532511287d, 0.130526192220052d, -0.99144486137381d,
            -0.130526192220052d, -0.99144486137381d, -0.38268343236509d, -0.923879532511287d, -0.608761429008721d, -0.793353340291235d,
            -0.793353340291235d, -0.608761429008721d, -0.923879532511287d, -0.38268343236509d, -0.99144486137381d, -0.130526192220052d,
            -0.99144486137381d, 0.130526192220051d, -0.923879532511287d, 0.38268343236509d, -0.793353340291235d, 0.608761429008721d,
            -0.608761429008721d, 0.793353340291235d, -0.38268343236509d, 0.923879532511287d, -0.130526192220052d, 0.99144486137381d,
            0.130526192220052d, 0.99144486137381d, 0.38268343236509d, 0.923879532511287d, 0.608761429008721d, 0.793353340291235d,
            0.793353340291235d, 0.608761429008721d, 0.923879532511287d, 0.38268343236509d, 0.99144486137381d, 0.130526192220051d,
            0.99144486137381d, -0.130526192220051d, 0.923879532511287d, -0.38268343236509d, 0.793353340291235d, -0.60876142900872d,
            0.608761429008721d, -0.793353340291235d, 0.38268343236509d, -0.923879532511287d, 0.130526192220052d, -0.99144486137381d,
            -0.130526192220052d, -0.99144486137381d, -0.38268343236509d, -0.923879532511287d, -0.608761429008721d, -0.793353340291235d,
            -0.793353340291235d, -0.608761429008721d, -0.923879532511287d, -0.38268343236509d, -0.99144486137381d, -0.130526192220052d,
            -0.99144486137381d, 0.130526192220051d, -0.923879532511287d, 0.38268343236509d, -0.793353340291235d, 0.608761429008721d,
            -0.608761429008721d, 0.793353340291235d, -0.38268343236509d, 0.923879532511287d, -0.130526192220052d, 0.99144486137381d,
            0.130526192220052d, 0.99144486137381d, 0.38268343236509d, 0.923879532511287d, 0.608761429008721d, 0.793353340291235d,
            0.793353340291235d, 0.608761429008721d, 0.923879532511287d, 0.38268343236509d, 0.99144486137381d, 0.130526192220051d,
            0.99144486137381d, -0.130526192220051d, 0.923879532511287d, -0.38268343236509d, 0.793353340291235d, -0.60876142900872d,
            0.608761429008721d, -0.793353340291235d, 0.38268343236509d, -0.923879532511287d, 0.130526192220052d, -0.99144486137381d,
            -0.130526192220052d, -0.99144486137381d, -0.38268343236509d, -0.923879532511287d, -0.608761429008721d, -0.793353340291235d,
            -0.793353340291235d, -0.608761429008721d, -0.923879532511287d, -0.38268343236509d, -0.99144486137381d, -0.130526192220052d,
            -0.99144486137381d, 0.130526192220051d, -0.923879532511287d, 0.38268343236509d, -0.793353340291235d, 0.608761429008721d,
            -0.608761429008721d, 0.793353340291235d, -0.38268343236509d, 0.923879532511287d, -0.130526192220052d, 0.99144486137381d,
            0.130526192220052d, 0.99144486137381d, 0.38268343236509d, 0.923879532511287d, 0.608761429008721d, 0.793353340291235d,
            0.793353340291235d, 0.608761429008721d, 0.923879532511287d, 0.38268343236509d, 0.99144486137381d, 0.130526192220051d,
            0.99144486137381d, -0.130526192220051d, 0.923879532511287d, -0.38268343236509d, 0.793353340291235d, -0.60876142900872d,
            0.608761429008721d, -0.793353340291235d, 0.38268343236509d, -0.923879532511287d, 0.130526192220052d, -0.99144486137381d,
            -0.130526192220052d, -0.99144486137381d, -0.38268343236509d, -0.923879532511287d, -0.608761429008721d, -0.793353340291235d,
            -0.793353340291235d, -0.608761429008721d, -0.923879532511287d, -0.38268343236509d, -0.99144486137381d, -0.130526192220052d,
            -0.99144486137381d, 0.130526192220051d, -0.923879532511287d, 0.38268343236509d, -0.793353340291235d, 0.608761429008721d,
            -0.608761429008721d, 0.793353340291235d, -0.38268343236509d, 0.923879532511287d, -0.130526192220052d, 0.99144486137381d,
            0.130526192220052d, 0.99144486137381d, 0.38268343236509d, 0.923879532511287d, 0.608761429008721d, 0.793353340291235d,
            0.793353340291235d, 0.608761429008721d, 0.923879532511287d, 0.38268343236509d, 0.99144486137381d, 0.130526192220051d,
            0.99144486137381d, -0.130526192220051d, 0.923879532511287d, -0.38268343236509d, 0.793353340291235d, -0.60876142900872d,
            0.608761429008721d, -0.793353340291235d, 0.38268343236509d, -0.923879532511287d, 0.130526192220052d, -0.99144486137381d,
            -0.130526192220052d, -0.99144486137381d, -0.38268343236509d, -0.923879532511287d, -0.608761429008721d, -0.793353340291235d,
            -0.793353340291235d, -0.608761429008721d, -0.923879532511287d, -0.38268343236509d, -0.99144486137381d, -0.130526192220052d,
            -0.99144486137381d, 0.130526192220051d, -0.923879532511287d, 0.38268343236509d, -0.793353340291235d, 0.608761429008721d,
            -0.608761429008721d, 0.793353340291235d, -0.38268343236509d, 0.923879532511287d, -0.130526192220052d, 0.99144486137381d,
            0.38268343236509d, 0.923879532511287d, 0.923879532511287d, 0.38268343236509d, 0.923879532511287d, -0.38268343236509d,
            0.38268343236509d, -0.923879532511287d, -0.38268343236509d, -0.923879532511287d, -0.923879532511287d, -0.38268343236509d,
            -0.923879532511287d, 0.38268343236509d, -0.38268343236509d, 0.923879532511287d,
    };

    protected static final double[] GRADIENTS_3D = {
            0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0,
            1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0,
            1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0,
            0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0,
            1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0,
            1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0,
            0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0,
            1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0,
            1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0,
            0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0,
            1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0,
            1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0,
            0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0,
            1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0,
            1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0,
            1, 1, 0, 0, 0, -1, 1, 0, -1, 1, 0, 0, 0, -1, -1, 0
    };

    public SimplexStyleSampler(int seed) {
        super(seed);
    }

    protected static double gradCoord(int seed, int xPrimed, int yPrimed, double xd, double yd) {
        int hash = hash(seed, xPrimed, yPrimed);
        hash ^= hash >> 15;
        hash &= 127 << 1;

        double xg = GRADIENTS_2_D[hash];
        double yg = GRADIENTS_2_D[hash | 1];

        return xd * xg + yd * yg;
    }

    protected static double gradCoord(int seed, int xPrimed, int yPrimed, int zPrimed, double xd, double yd, double zd) {
        int hash = hash(seed, xPrimed, yPrimed, zPrimed);
        hash ^= hash >> 15;
        hash &= 63 << 2;

        double xg = GRADIENTS_3D[hash];
        double yg = GRADIENTS_3D[hash | 1];
        double zg = GRADIENTS_3D[hash | 2];

        return xd * xg + yd * yg + zd * zg;
    }
}