package live.connector.vertxui.samples.server.todomvc;

import java.lang.invoke.MethodHandles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import live.connector.vertxui.samples.client.figwheely.Client;
import live.connector.vertxui.samples.client.todomvc.View;
import live.connector.vertxui.samples.server.AllExamplesServer;
import live.connector.vertxui.server.FigStaticHandler;
import live.connector.vertxui.server.FigWheely;
import live.connector.vertxui.server.VertxUI;

public class Server extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(MethodHandles.lookup().lookupClass().getName());
	}

	@Override
	public void start() {
		// Initialize the router and a webserver with HTTP-compression
		Router router = Router.router(vertx);
		HttpServer server = vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true));

		// Serve the javascript for FigWheely
		router.get(Client.figLocation).handler(FigWheely.create());

		// This application is also an example for using an existing index.html.
		// The only thing is that there is a slight
		// different setup for an existing HTML: call VertxUI.with()
		// with a null URL (third parameter) and server /a/ manually
		VertxUI.with(View.class, null, true, false);
		router.get("/a/*").handler(StaticHandler.create("war/a"));
		router.get("/*").handler(FigStaticHandler.create("assets/todos", "/"));
		AllExamplesServer.startWarAndServer2(router, server);
	}

}