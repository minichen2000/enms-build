package com.nsb.enms.restful.adapterserver.api.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class NesApiServiceImplTest {
	private String url = "http://localhost:8002";
	CloseableHttpClient httpclient = HttpClients.createDefault();

	@BeforeClass
	public static void setUpClass() {
	}

	@Test
	public void testAddNe() {
		try {
			createNe();
		} catch (Exception e) {
			fail("failed to create ne, " + e.getMessage());
		}

		assertTrue("create ok", true);
	}

	@Test
	public void testDeleteNe() {
		String neId = "47000580000000000000010001002060620A021D";
		try {
			deleteNe(neId);
		} catch (Exception e) {
			fail("failed to delete ne, " + e.getMessage());
		}

		assertTrue("delete ok", true);
	}

	@Test
	public void testGetNeById() {
		try {
			String neId = "47000580000000000000010001002060620A021D";
			getNeById(neId);
		} catch (Exception e) {
			fail("failed to getNeById" + e.getMessage());
		}

		assertTrue("getNeById ok", true);
	}

	@Test
	public void testGetNes() {
		try {
			getNes();
		} catch (Exception e) {
			fail("failed to getNes" + e.getMessage());
		}

		assertTrue("create ok", true);
	}

	@Test
	public void testUpdateNe() {
		try {
			updateNe();
		} catch (Exception e) {
			fail("failed to getNes" + e.getMessage());
		}
	}

	private void updateNe() throws Exception {
		CloseableHttpResponse response = null;
		HttpPatch httpPatch = new HttpPatch(url + "/nes");
		InputStream is = getClass().getResourceAsStream("/updateNe.json");
		String json = stream2String(is);
		System.out.println(json);
		httpPatch.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
		response = httpclient.execute(httpPatch);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();

		// InputStream is = entity.getContent();
		// String json = stream2String(is);
		// System.out.println("updateNe:" + json);

		EntityUtils.consume(entity);
		response.close();
	}

	private void getNeById(String neId) throws Exception {
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet(url + "/nes/" + neId);
		response = httpclient.execute(httpGet);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();

		InputStream is = entity.getContent();
		String json = stream2String(is);
		System.out.println("getNeById:" + json);

		EntityUtils.consume(entity);
		response.close();
	}

	private void getNes() throws Exception {
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet(url + "/nes");
		response = httpclient.execute(httpGet);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();

		InputStream is = entity.getContent();
		String json = stream2String(is);
		System.out.println("getNes:" + json);

		EntityUtils.consume(entity);
		response.close();
	}

	public static void main(String args[]) {
		try {
			NesApiServiceImplTest api = new NesApiServiceImplTest();
			// api.getNes();
//			api.testAddNe();
			// api.testDeleteNe();
			api.testGetNeById();
			api.testUpdateNe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createNe() throws Exception {
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url + "/nes");
		InputStream is = getClass().getResourceAsStream("/createNe.json");
		String json = stream2String(is);
		System.out.println(json);

		httpPost.setEntity(new StringEntity(json, "UTF-8"));
		httpPost.setHeader("Content-Type", "application/json");
		response = httpclient.execute(httpPost);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		EntityUtils.consume(entity);
		response.close();
	}

	private void deleteNe(String neId) throws Exception {
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = new HttpDelete(url + "/nes/" + neId);
		response = httpclient.execute(httpDelete);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		EntityUtils.consume(entity);
		response.close();
	}

	private String stream2String(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		try {
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = baos.toString();
		return str;
	}
}
