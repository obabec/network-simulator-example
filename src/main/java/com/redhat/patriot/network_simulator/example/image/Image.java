package com.redhat.patriot.network_simulator.example.image;

import java.util.Set;

/**
 * The interface Image.
 */
public interface Image {
    /**
     * Build image.
     *
     * @param tags        the tags
     * @param imageSource the image source
     */
    void buildImage(Set<String> tags, String imageSource);

    /**
     * Delete image.
     *
     * @param tags the tags
     */
    void deleteImage(Set<String> tags);
}
