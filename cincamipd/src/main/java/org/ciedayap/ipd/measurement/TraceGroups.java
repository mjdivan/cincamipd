/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ciedayap.ipd.measurement;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.ciedayap.ipd.Containers;
import org.ciedayap.ipd.Level;
import org.ciedayap.ipd.exception.ProcessingException;
import org.ciedayap.ipd.utils.Tokens;
import org.ciedayap.ipd.utils.StringUtils;
import org.ciedayap.ipd.utils.TokenDifference;
import org.ciedayap.utils.TranslateJSON;
import org.ciedayap.utils.TranslateXML;
/**
 *
 * @author mjdivan
 */
@XmlRootElement(name="TraceGroups")
@XmlType(propOrder={"traceGroups"})
public class TraceGroups implements Serializable, Level, Containers{
    private java.util.ArrayList<TraceGroup>  traceGroups;
    
    public TraceGroups()
    {
        traceGroups=new ArrayList<>();
    }
    
    @Override
    public boolean realeaseResources() throws ProcessingException    
    {
        if(traceGroups==null)  return true;
        for(int i=0;i<traceGroups.size();i++)
        {
            traceGroups.get(i).realeaseResources();
        }
        
        traceGroups.clear();
        traceGroups=null;

        return true;
    }
    
    public static synchronized TraceGroups create(ArrayList<TraceGroup> list)
    {
        TraceGroups tgs=new TraceGroups();
        tgs.setTraceGroups(list);
        
        return tgs;
    }
    
    @Override
    public int getLevel() {
        return 11;
    }

    @Override
    public String writeTo() throws ProcessingException {
        if(!this.isDefinedProperties()) return null;
        if(this.getTraceGroups().isEmpty()) throw new ProcessingException("The list is empty");
        
        StringBuilder sb=new StringBuilder();
        sb.append(Tokens.startLevel);
        for(TraceGroup item:traceGroups)
        {
            String segment=item.writeTo();
            
            if(!StringUtils.isEmpty(segment))
            {
                sb.append(segment);
            }
        }
        sb.append(Tokens.endLevel);
        
        return sb.toString();
    }

    @Override
    public Object readFrom(String text) throws ProcessingException {
        return readFromSt(text);
    }
    
    public static TraceGroups readFromSt(String text) throws ProcessingException {    
        if(StringUtils.isEmpty(text)) return null;
        
        int idx_st=text.indexOf(Tokens.startLevel);
        int idx_en=text.lastIndexOf(Tokens.endLevel);
        if(idx_st<0 || idx_en<0) return null;
        
        TraceGroups item=new TraceGroups();
        
        //It removes the startLevel and endLevel characters
        String cleanedText=text.substring(idx_st+1, idx_en);        

        idx_st=cleanedText.indexOf(Tokens.startLevel);
        idx_en=cleanedText.indexOf(Tokens.endLevel);
        while(idx_st>=0 && idx_en>=0)
        {
            String segment=cleanedText.substring(0, idx_en+1);            
            TraceGroup tg=null;
            try{
                tg=(TraceGroup) TraceGroup.readFromSt(segment);
            }catch(ProcessingException pe)
            {
                tg=null;
            }
            
            if(tg!=null) item.getTraceGroups().add(tg);
            
            //It retrieves the rest of the string
            cleanedText=cleanedText.substring(idx_en+1);
            idx_st=cleanedText.indexOf(Tokens.startLevel);
            idx_en=cleanedText.indexOf(Tokens.endLevel);
        }

        return item;
    }

    @Override
    public boolean isDefinedProperties() {
        return !(this.traceGroups==null || this.traceGroups.isEmpty());
    }

    @Override
    public String getCurrentClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getQualifiedCurrentClassName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public String getUniqueID() {
        if(!this.isDefinedProperties()) return null;
        
        StringBuilder sb=new StringBuilder();
        for (TraceGroup item : traceGroups) 
        {
            sb.append(item.getTraceGroupID()).append("-");
        }
        
        return sb.toString();
    }

    @Override
    public int length() {
        return (traceGroups!=null)?traceGroups.size():0;
    }

    @Override
    public boolean isEmpty() {
        if(traceGroups==null) return true;
        
        return traceGroups.isEmpty();
    }

    @Override
    public Class getKindOfElement() {
        return TraceGroup.class;
    }

    /**
     * @return the traceGroups
     */
    @XmlElement(name="TraceGroup", required=true)
    public java.util.ArrayList<TraceGroup> getTraceGroups() {
        return traceGroups;
    }

    /**
     * @param traceGroups the traceGroups to set
     */
    public void setTraceGroups(java.util.ArrayList<TraceGroup> traceGroups) {
        if(this.traceGroups!=null){
            try{
                this.realeaseResources();
            }catch(ProcessingException pe){}
        }
        
        this.traceGroups = traceGroups;
    }
    
    public static TraceGroups test(int k)
    {
        TraceGroups list=new  TraceGroups();
        
        for(int i=0;i<k;i++)
        {
            TraceGroup u=new TraceGroup();
            u.setTraceGroupID("ID;"+String.valueOf(i));
            u.setName("Peter"+String.valueOf(i));
            u.setDefinition("Some description "+String.valueOf(i));
            
            list.getTraceGroups().add(u);
        }
        
        return list;
    }
    
    @Override
    public String computeFingerprint(){
        if(!this.isDefinedProperties()) return null;
        
        String original;
        try {
            original = this.writeTo();
        } catch (ProcessingException ex) {
            return null;
        }
        
        return (original==null)?null:Tokens.getFingerprint(original);
    }    
    
    @Override
    public int hashCode()
    {
        String ret=computeFingerprint();
        return (ret==null)?0:ret.hashCode();
    }
    
    @Override
    public boolean equals(Object target)
    {
        if(target==null) return false;
        if(!(target instanceof TraceGroups)) return false;
        
        TraceGroups cp=(TraceGroups)target;
        String ft=cp.computeFingerprint();
        
        String or;
        try{
            or=Tokens.getFingerprint(this.writeTo());
        }catch(ProcessingException pe)
        {
            return false;
        }
        
        return (ft==null)?false:(ft.equalsIgnoreCase(or));
    }
    
    @Override
    public ArrayList<TokenDifference> findDifferences(Object ptr) throws ProcessingException {
        if(ptr==null) throw new ProcessingException("The instance to compared is null");
        if(!(ptr instanceof TraceGroups)) throw new ProcessingException("The instance to be compared is not of the expected type");
        TraceGroups comp=(TraceGroups)ptr;
        if(!comp.isDefinedProperties()) throw new ProcessingException("The instance to be compared does not have the properties defined");
        if(!this.isDefinedProperties()) throw new ProcessingException("The instance to compare does not have the properties defined");
        
        if(this.equals(comp)) return null;                
        if(this.traceGroups.size()!=comp.getTraceGroups().size())
            return TokenDifference.createAsAList(TraceGroups.class, this.getUniqueID(), this.getLevel(), this.computeFingerprint(), comp.computeFingerprint());
        
        ArrayList<TokenDifference> global=new ArrayList();
        for(int i=0;i<traceGroups.size();i++)
        {
            TraceGroup pthis=traceGroups.get(i);
            TraceGroup pcomp=comp.getTraceGroups().get(i);
            
            ArrayList<TokenDifference> result=pthis.findDifferences(pcomp);
            if(result!=null && result.size()>0)
            {
                global.addAll(result);            
                result.clear();
            }
        }
        
        return (global.size()>0)?global:null;
    }
    
    public static void main(String arg[]) throws ProcessingException
    {
        TraceGroups list=test(4);
        
        String xml=TranslateXML.toXml(list);
        String json=TranslateJSON.toJSON(list);
        String brief=list.writeTo();
        
        System.out.println(xml);
        System.out.println(json);
        System.out.println(brief);
        
        TraceGroups tgs=(TraceGroups) TraceGroups.readFromSt(brief);
        for(TraceGroup tg:tgs.getTraceGroups())
        {
            System.out.println("ID: "+tg.getTraceGroupID());
            System.out.println("Name: "+tg.getName());
            System.out.println("Definition: "+tg.getDefinition());
        }
        
        System.out.println("Equal: "+list.equals(tgs));
    }
    
}
