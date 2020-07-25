package com.bc.fileupload.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author hp
 */
@FunctionalInterface
public interface SaveHandler{

    void save(InputStream source, Path target) throws IOException;
}
