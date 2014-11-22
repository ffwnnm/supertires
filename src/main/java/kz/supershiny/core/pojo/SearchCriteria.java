/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.util.Constants;

/**
 *
 * @author kilrwhle
 */
public class SearchCriteria implements Serializable {
    
    protected int pageSize = Constants.ITEMS_PER_PAGE;
    protected long begin = 0;
    protected long current = 1;
    protected long total = 0;
    protected long resultSize = 0;
    protected int maxNumbersSize = Constants.PAGING_SIZE;
    protected List<Long> numbers;
    
    public void clear() {
        current = 1;
        begin = 0;
        pageSize = Constants.ITEMS_PER_PAGE;
        total = 0;
        resultSize = 0;
        maxNumbersSize = Constants.PAGING_SIZE;
        if (numbers == null) {
            numbers = new ArrayList<Long>();
        } else {
            numbers.clear();
        }
    }
    
    public void resetPaging() {
        this.current = 1;
        this.begin = 0;
        getNumbers();
    };
    
    public void refreshPaging() {
        getNumbers();
    };
    
    public List<Long> getNumbers() {
        if (Math.ceil((double) total / pageSize) >= Constants.PAGING_SIZE) {
            maxNumbersSize = Constants.PAGING_SIZE;
        } else {
            maxNumbersSize = (int) Math.ceil((double) total / pageSize);
        }
        if (maxNumbersSize <= 0) {
            maxNumbersSize = 1;
        }
        if (numbers == null) {
            numbers = new ArrayList<Long>();
        } else {
            numbers.clear();
        }
        long shift = this.current - (int) Math.ceil(maxNumbersSize / 2.0D) < 0 
                ? 0 
                : this.current - (int) Math.ceil(maxNumbersSize / 2.0D);
        if (shift + maxNumbersSize > (int) Math.ceil((double) total / pageSize)) {
            shift = (int) Math.ceil((double) total / pageSize) - maxNumbersSize;
            if (shift < 0) shift = 0;
        }
        for (int i=0; i<maxNumbersSize; i++) {
            numbers.add(i + 1 + shift);
        }
        return numbers;
    }
    
    public void next() {
        if (this.current < (long) Math.ceil((double) total / pageSize)) {
            this.current++;
            this.begin += pageSize;
        }
    }
    
    public void prev() {
        if (this.current - 1 > 0) {
            this.current--;
            this.begin -= pageSize;
        }
    }
    
    public void snext() {
        this.current = (long) Math.ceil((double) total / pageSize);
        if (this.current < 1) this.current = 1;
        this.begin = (this.current - 1) * pageSize;
    }
    
    public void sprev() {
        this.current = 1;
        this.begin = 0;
    }
    
    public void toPage(long target) {
        this.current = target;
        this.begin = (this.current - 1) * pageSize;
    }
    
    public String getFromTo() {
        return getLow() + " - " + getHigh();
    }
    
    public long getEnd() {
        return this.begin + pageSize < total ? this.begin + pageSize : total - 1;
    }
    
    //from
    public long getLow() {
        return begin + 1;
    }
    
    //to
    public long getHigh() {
        return getEnd() + 1 < total ? getEnd() : total;
    }

    @Override
    public String toString() {
        return "total: " + total + ", current page: " + current + ", begin: " + begin +
                ", end: " + getEnd() + ", resultSize: " + resultSize;
    }
    
    public boolean isLastPage() {
        return this.current == (long) Math.ceil((double) total / pageSize);
    }
    
    public boolean isFirstPage() {
        return this.current == 1;
    }

    public int getMaxNumbersSize() {
        return maxNumbersSize;
    }

    public void setMaxNumbersSize(int maxNumbersSize) {
        this.maxNumbersSize = maxNumbersSize;
    }

    public long getResultSize() {
        return resultSize;
    }

    public void setResultSize(long resultSize) {
        this.resultSize = resultSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
