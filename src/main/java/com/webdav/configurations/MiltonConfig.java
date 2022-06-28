public class MiltonConfig extends DefaultMiltonConfigurator {
    private SecurityManager securityManager;

    public MiltonConfig() {
        this.securityManager = new CustomSecurityManager();
    }

    @Override
    protected void build() {
        builder.setEnableBasicAuth(true);
        builder.setAuthenticationHandlers(Collections.singletonList(new SecurityManagerBasicAuthHandler(this.securityManager)));
        builder.setSecurityManager(this.securityManager);
        super.build();
    }
}