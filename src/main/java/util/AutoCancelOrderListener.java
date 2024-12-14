package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import database.OrderDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


@javax.servlet.annotation.WebListener
public  class AutoCancelOrderListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Bat dau Tu dong huy don khong hop le...");
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            OrderDAO orderDAO = new OrderDAO();
            int rowsUpdated = orderDAO.cancelExpiredOrders();
            System.out.println("So don hang da bi huy tu dong: " + rowsUpdated);
//        }, 0, 1, TimeUnit.MINUTES); // Lặp mỗi 1 phút
        }, 0, 1, TimeUnit.HOURS); // Lặp mỗi 1 giờ
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Dung tu dong huy don khong hop le...");
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
