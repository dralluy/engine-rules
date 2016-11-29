package org.sbol.projects.engine.utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Cargador de clases de S3. Se debe subir el fichero .class con la siguiente nomenclatura:
 * Nombre package + clase
 *
 * Ejemplo: org.mango.services.sercat.FijarPrecioRule (sin el .class)
 *
 * Como resultado tenemos el objeto Class que representa a la clase.
 *
 * @author david.ralluy
 *
 */
public class S3ClassLoader extends ClassLoader {

    private static Logger logger = LoggerFactory.getLogger(S3ClassLoader.class);

    private static final String S3_URL_PREFIX = "https://s3.amazonaws.com/";

    private AmazonS3Client s3;
    private String bucketName;

    /**
     * Constructs S3 bucket.
     *
     * @param bucketName Bucket name
     * @param region AWS region
     * @param classLoader Classloader
     */
    public S3ClassLoader(final String bucketName, final Region region, final ClassLoader classLoader) {
        super(classLoader);
        this.bucketName = bucketName;
        this.s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
        this.s3.setRegion(region);
    }

    @Override
    public URL getResource(final String name) {
        URL url = null;
        try {
            url = new URL(S3ClassLoader.S3_URL_PREFIX + this.bucketName + "/" + name);
        } catch (MalformedURLException e) {
            S3ClassLoader.logger.error("Resource error: ", e);
        }
        return url;
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        return this.s3.getObject(this.bucketName, name).getObjectContent();
    }

    /**
     * Get bucket name.
     *
     * @return Bucket name
     */
    public String getBucketName() {
        return this.bucketName;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classBytes = this.getClassBytes(name);
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] getClassBytes(final String key) throws ClassNotFoundException {
        try {
            int contentLength = (int) this.s3.getObjectMetadata(this.bucketName, key).getContentLength();
            byte[] bytes = new byte[contentLength];
            int bytesCount = this.getResourceAsStream(key).read(bytes);
            if (bytesCount > 0) {
                return bytes;
            }
        } catch (Exception t) {
            throw new ClassNotFoundException(
                    "Unable to find class resource for key \"" + key + "\" in S3 Bucket \"" + this.bucketName + "\"",
                    t);
        }
        return new byte[0];
    }
}
