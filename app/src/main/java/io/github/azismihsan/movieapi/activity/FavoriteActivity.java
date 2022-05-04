 Hanna
 */
public class CertStore {
    /*
     * Constant to lookup in the Security properties file to determine
     * the default certstore type. In the Security properties file, the
     * default certstore type is given as:
     * <pre>
     * certstore.type=LDAP
     * </pre>
     */
    private static final String CERTSTORE_TYPE = "certstore.type";
    private CertStoreSpi storeSpi;
    private Provider provider;
    private String type;
    private CertStoreParameters params;

    /**
     * Creates a <code>CertStore</code> object of the given type, and
     * encapsulates the given provider implementation (SPI object) in it.
     *
     * @param storeSpi the provider implementation
     * @param provider the provider
     * @param type the type
     * @param params the initialization parameters (may be <code>null</code>)
     */
    protected CertStore(CertStoreSpi storeSpi, Provider provider,
                        String type, CertStoreParameters params) {
        this.storeSpi = storeSpi;
        this.provider = provider;
        this.type = type;
        if (params != null)
            this.params = (CertStoreParameters) params.clone();
    }

    /**
     * Returns a <code>Collection</code> of <code>Certificate</code>s that
     * match the specified selector. If no <code>Certificate</code>s
     * match the selector, an empty <code>Collection</code> will be returned.
     * <p>
     * For some <code>CertStore</code> types, the resulting
     * <code>Collection</code> may not contain <b>all</b> of the
     * <code>Certificate</code>s that match the selector. For instance,
     * an LDAP <code>CertStore</code> may not search all entries in the
     * directory. Instead, it may just search entries that are likely to
     * contain the <code>Certificate</code>s it is looking for.
     * <p>
     * Some <code>CertStore</code> implementations (especially LDAP
     * <code>CertStore</code>s) may throw a <code>CertStoreException</code>
     * unless a non-nul