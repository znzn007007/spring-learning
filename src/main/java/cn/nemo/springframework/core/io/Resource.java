package cn.nemo.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zkl
 */
public interface Resource {

	InputStream getInputStream() throws IOException;
}