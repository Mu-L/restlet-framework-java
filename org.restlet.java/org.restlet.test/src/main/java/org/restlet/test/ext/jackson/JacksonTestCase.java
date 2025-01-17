/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.test.RestletTestCase;

/**
 * Unit test for the Jackson extension.
 *
 * @author Jerome Louvel
 */
public class JacksonTestCase extends RestletTestCase {

    protected Customer createCustomer() {
        Date date = new Date(1356533333882L);

        Customer result = new Customer();
        result.setFirstName("Foo");
        result.setLastName("Bar");

        Invoice invoice = new Invoice();
        invoice.setAmount(12456);
        invoice.setDate(date);
        invoice.setPaid(false);
        result.getInvoices().add(invoice);

        invoice = new Invoice();
        invoice.setAmount(7890);
        invoice.setDate(date);
        invoice.setPaid(true);
        result.getInvoices().add(invoice);

        return result;
    }

    protected Invoice createInvoice() {
        Date date = new Date(1356533333882L);
        Invoice invoice = new Invoice();
        invoice.setAmount(12456);
        invoice.setDate(date);
        invoice.setPaid(false);
        return invoice;
    }

    @Test
    public void testCsv() throws Exception {
        Invoice invoice = createInvoice();
        JacksonRepresentation<Invoice> rep = new JacksonRepresentation<>(
                MediaType.TEXT_CSV, invoice);
        String text = rep.getText();
        assertEquals("12456,1356533333882,false\n", text);
        rep = new JacksonRepresentation<>(new StringRepresentation(text,
                rep.getMediaType()), Invoice.class);
        verify(invoice, rep.getObject());
    }

    @Test
    public void testException() throws Exception {
        Customer customer = createCustomer();

        MyException me = new MyException(customer, "CUST-1234");

        // Unless we are in debug mode, hide those properties
        me.setStackTrace(new StackTraceElement[0]);

        JacksonRepresentation<MyException> rep = new JacksonRepresentation<>(MediaType.APPLICATION_JSON, me);

        rep = new JacksonRepresentation<>(new StringRepresentation(rep.getText(), rep.getMediaType()), MyException.class);
        verify(me, rep.getObject());
    }

    @Test
    public void testJson() throws Exception {
        Customer customer = createCustomer();
        JacksonRepresentation<Customer> rep = new JacksonRepresentation<>(MediaType.APPLICATION_JSON, customer);
        String text = rep.getText();
        assertEquals(
                "{\"firstName\":\"Foo\",\"lastName\":\"Bar\",\"invoices\":[{\"date\":1356533333882,\"amount\":12456,\"paid\":false},{\"date\":1356533333882,\"amount\":7890,\"paid\":true}]}",
                text);

        rep = new JacksonRepresentation<>(new StringRepresentation(
                text, rep.getMediaType()), Customer.class);
        verify(customer, rep.getObject());
    }

    @Test
    public void testSmile() throws Exception {
        Customer customer = createCustomer();
        JacksonRepresentation<Customer> rep = new JacksonRepresentation<>(
                MediaType.APPLICATION_JSON_SMILE, customer);
        rep = new JacksonRepresentation<>(rep, Customer.class);
        verify(customer, rep.getObject());
    }

    @Test
    public void testXml() throws Exception {
        Customer customer = createCustomer();
        JacksonRepresentation<Customer> rep = new JacksonRepresentation<>(
                MediaType.APPLICATION_XML, customer);
        String text = rep.getText();
        assertEquals(
                "<Customer><firstName>Foo</firstName><lastName>Bar</lastName><invoices><invoices><date>1356533333882</date><amount>12456</amount><paid>false</paid></invoices><invoices><date>1356533333882</date><amount>7890</amount><paid>true</paid></invoices></invoices></Customer>",
                text);
        rep = new JacksonRepresentation<>(new StringRepresentation(
                text, rep.getMediaType()), Customer.class);
        verify(customer, rep.getObject());
    }

    @Test
    public void testXmlBomb() {
        ClientResource cr = new ClientResource(
                "clap://class/org/restlet/test/ext/jackson/jacksonBomb.xml");
        Representation xmlRep = cr.get();
        xmlRep.setMediaType(MediaType.APPLICATION_XML);
        boolean error = false;
        try {
            new JacksonRepresentation<>(xmlRep, Customer.class)
                    .getObject();
        } catch (Exception e) {
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void testYaml() throws Exception {
        Customer customer = createCustomer();
        JacksonRepresentation<Customer> rep = new JacksonRepresentation<>(
                MediaType.APPLICATION_YAML, customer);
        String text = rep.getText();
        assertEquals("---\n" + "firstName: \"Foo\"\n" + "lastName: \"Bar\"\n"
                + "invoices:\n" + "- date: 1356533333882\n"
                + "  amount: 12456\n" + "  paid: false\n"
                + "- date: 1356533333882\n" + "  amount: 7890\n"
                + "  paid: true\n", text);
        rep = new JacksonRepresentation<>(new StringRepresentation(
                text, rep.getMediaType()), Customer.class);
        verify(customer, rep.getObject());
    }

    protected void verify(Customer customer1, Customer customer2) {
        assertEquals(customer1.getFirstName(), customer2.getFirstName());
        assertEquals(customer1.getLastName(), customer2.getLastName());
        assertEquals(customer1.getInvoices().size(), customer2.getInvoices()
                .size());
        assertEquals(customer1.getInvoices().get(0).getAmount(), customer2
                .getInvoices().get(0).getAmount());
        assertEquals(customer1.getInvoices().get(1).getAmount(), customer2
                .getInvoices().get(1).getAmount());
        assertEquals(customer1.getInvoices().get(0).getDate(), customer2
                .getInvoices().get(0).getDate());
        assertEquals(customer1.getInvoices().get(1).getDate(), customer2
                .getInvoices().get(1).getDate());
    }

    protected void verify(Invoice invoice1, Invoice invoice2) {
        assertEquals(invoice1.getAmount(), invoice2.getAmount());
        assertEquals(invoice1.getDate(), invoice2.getDate());
    }

    protected void verify(MyException me1, MyException me2) {
        assertEquals(me1.getErrorCode(), me2.getErrorCode());
        verify(me1.getCustomer(), me2.getCustomer());
    }
}
