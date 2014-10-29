/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;

/**
 *
 * @author kilrwhle
 */
public class TiresAutocompleteTextField<T> extends AutoCompleteTextField<T> {

    private List<T> choices;

    public TiresAutocompleteTextField(String id, Class<T> type, List<T> choices) {
        super(id, type);
        this.choices = choices;
    }
    
    

    @Override
    protected Iterator<T> getChoices(String input) {
        if (input.isEmpty()) {
            return Collections.emptyListIterator();
        }
        if (input.trim().isEmpty()) {
            return choices.iterator();
        }

        List<T> subChoices = new ArrayList<T>();
        for (T t : choices) {
            if (t.toString().toUpperCase().startsWith(input.toUpperCase())) {
                subChoices.add(t);
            }
        }
        return subChoices.iterator();
    }

}
