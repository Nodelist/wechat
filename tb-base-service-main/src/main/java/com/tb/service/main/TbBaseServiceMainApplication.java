package com.tb.service.main;

import com.sticker.online.core.handlerfactory.RouterHandlerFactory;
import com.sticker.online.core.vertx.DeployVertxServer;
import com.sticker.online.core.vertx.VertxUtil;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication(scanBasePackages={"com.tb.service","com.sticker.online"})
@EntityScan({"com.tb.service","com.sticker.online"})
@EnableJpaRepositories({"com.tb.service","com.sticker.online"})
@EnableTransactionManagement
public class TbBaseServiceMainApplication {
    /**
     * web api所在包路径
     */
    @Value("${web-api-packages}")
    private String webApiPackages;

    /**
     * 异步服务所在包路径
     */
    @Value("${async-service-impl-packages}")
    private String asyncServiceImplPackages;

    /**
     * http服务器端口号
     */
    @Value("${http-server-port}")
    private int httpServerPort;

    /**
     * 工作线程池大小（可根据实际情况调整）
     */
    @Value("${worker-pool-size}")
    private int workerPoolSize;

    /**
     * 异步服务实例数量（建议和CPU核数相同）
     */
    @Value("${async-service-instances}")
    private int asyncServiceInstances;
    public static void main(String[] args) {
        SpringApplication.run(TbBaseServiceMainApplication.class, args);
    }
    @EventListener
    public void deployVerticles(ApplicationReadyEvent event) {
        EventBusOptions eventBusOptions = new EventBusOptions();
        //便于调试 设定超时等时间较长 生产环境建议适当调整
        eventBusOptions.setConnectTimeout(1000 * 60 * 30);
        Vertx vertx = Vertx.vertx(
                new VertxOptions().setWorkerPoolSize(workerPoolSize)
                        .setEventBusOptions(eventBusOptions)
                        .setBlockedThreadCheckInterval(999999999L)
                        .setMaxEventLoopExecuteTime(Long.MAX_VALUE)
        );
        VertxUtil.init(vertx);
        try {

            Router router = new RouterHandlerFactory(webApiPackages).createRouter();
            DeployVertxServer.startDeploy(router, asyncServiceImplPackages,httpServerPort, asyncServiceInstances);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
