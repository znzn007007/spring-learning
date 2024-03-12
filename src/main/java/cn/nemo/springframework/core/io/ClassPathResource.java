package cn.nemo.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zkl
 */
public class ClassPathResource implements Resource {
	private final String path;

	private ClassLoader classLoader;

	public ClassPathResource(String path) {
		this(path, null);
	}

	public ClassPathResource(String path, ClassLoader classLoader) {
		this.path = path;
		this.classLoader = (classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream stream = classLoader.getResourceAsStream(path);
		if (stream == null) {
			throw new FileNotFoundException(path + " cannot be opened because it does not exist");
		}
		return stream;
	}
}
