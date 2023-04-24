package org.dummy.service.controllers;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.commons.test.TestResourceTracker;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.core.admin.embeddedserver.EmbeddedServerAdminOperationHandler;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;
import org.infinispan.server.hotrod.test.HotRodTestingUtil;
import org.junit.jupiter.api.extension.*;

import java.util.stream.Stream;

/**
 * Unit test extension to start an embedded Infinispan server for testing.
 *
 * Based on the examples from the following links:
 * @see <a href="https://github.com/infinispan/infinispan-test">infinispan-test</a>
 * @see <a href="https://github.build.ge.com/grid-integration-osb/ami-meter-filter">ami-meter-filter</a>
 *
 */
public class InfinispanServerExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, TestTemplateInvocationContextProvider {

  private HotRodServer hotRodServer;
  private final String host;
  private final String cacheName;
  private final int port;
  private final boolean startBeforeAll;
  private final boolean stopAfterAll;
  private final boolean startBeforeEach;
  private final boolean stopAfterEach;


  public InfinispanServerExtension(String cacheName, String host, int port, boolean startBeforeAll,
                                   boolean stopAfterAll, boolean startBeforeEach, boolean stopAfterEach) {
    this.host = host;
    this.port = port;
    this.cacheName = cacheName;
    this.startBeforeAll = startBeforeAll;
    this.stopAfterAll = stopAfterAll;
    this.startBeforeEach = startBeforeEach;
    this.stopAfterEach = stopAfterEach;
  }

  public InfinispanServerExtension(String host, int port, boolean startBeforeAll,
                                   boolean stopAfterAll, boolean startBeforeEach, boolean stopAfterEach) {
    this("default", host, port, startBeforeAll, stopAfterAll, startBeforeEach, stopAfterEach);
  }


  @Override
  public void afterAll(ExtensionContext extensionContext) throws Exception {
    if (stopAfterAll)
      stop();
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    if (stopAfterEach)
      stop();
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    if (startBeforeAll)
      start();
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    if (startBeforeEach)
      start();
  }

  @Override
  public boolean supportsTestTemplate(ExtensionContext extensionContext) {
    return true;
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
    return null;
  }

  private RemoteCacheManager hotRodClient;

  public RemoteCacheManager hotRodClient() {
    if (hotRodServer != null && hotRodClient == null) {
      org.infinispan.client.hotrod.configuration.ConfigurationBuilder builder = new org.infinispan.client.hotrod.configuration.ConfigurationBuilder();
      HotRodServerConfiguration serverConfiguration = hotRodServer.getConfiguration();
      builder.addServer().host(serverConfiguration.publicHost())
              .port(serverConfiguration.publicPort());
      builder.statistics().enable();
      builder.marshaller(new ProtoStreamMarshaller());
      hotRodClient = new RemoteCacheManager(builder.build());
    }
    return hotRodClient;
  }

  public void start() {
    if (hotRodServer == null) {
//      TestResourceTracker.setThreadTestName("InfinispanServer");
//      ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
//
//
//      EmbeddedCacheManager ecm = TestCacheManagerFactory.createCacheManager(
//              new GlobalConfigurationBuilder().nonClusteredDefault().defaultCacheName(cacheName),
//              configurationBuilder);
//
//      HotRodServerConfigurationBuilder serverBuilder = new HotRodServerConfigurationBuilder();
//      serverBuilder.adminOperationsHandler(new EmbeddedServerAdminOperationHandler());
//      hotRodServer = HotRodTestingUtil.startHotRodServer(ecm, host, port, serverBuilder);
    }
  }

  public void stop() {
    if (hotRodServer != null) {
      hotRodServer.stop();
    }
  }



}
