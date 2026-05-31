package io.github.dulidanci.isometricsokoban.level.util;

import io.github.dulidanci.isometricsokoban.block.Block;

public record ChangeEntry(BlockPos blockPos, Block oldBlock, Block newBlock) {

}
