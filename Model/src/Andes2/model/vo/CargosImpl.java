package Andes2.model.vo;

import oracle.jbo.Key;
import oracle.jbo.RowIterator;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Jul 01 19:51:02 CLT 2013
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CargosImpl extends EntityImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
     */
    public enum AttributesEnum {
        CrgoId {
            public Object get(CargosImpl obj) {
                return obj.getCrgoId();
            }

            public void put(CargosImpl obj, Object value) {
                obj.setCrgoId((String)value);
            }
        }
        ,
        CrgoNombre {
            public Object get(CargosImpl obj) {
                return obj.getCrgoNombre();
            }

            public void put(CargosImpl obj, Object value) {
                obj.setCrgoNombre((String)value);
            }
        }
        ,
        CrgoMetaCargo {
            public Object get(CargosImpl obj) {
                return obj.getCrgoMetaCargo();
            }

            public void put(CargosImpl obj, Object value) {
                obj.setCrgoMetaCargo((String)value);
            }
        }
        ,
        ListaSkillEdit {
            public Object get(CargosImpl obj) {
                return obj.getListaSkillEdit();
            }

            public void put(CargosImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static int firstIndex = 0;

        public abstract Object get(CargosImpl object);

        public abstract void put(CargosImpl object, Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int CRGOID = AttributesEnum.CrgoId.index();
    public static final int CRGONOMBRE = AttributesEnum.CrgoNombre.index();
    public static final int CRGOMETACARGO = AttributesEnum.CrgoMetaCargo.index();
    public static final int LISTASKILLEDIT = AttributesEnum.ListaSkillEdit.index();

    /**
     * This is the default constructor (do not remove).
     */
    public CargosImpl() {
    }


    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        return EntityDefImpl.findDefObject("Andes2.model.vo.Cargos");
    }

    /**
     * Gets the attribute value for CrgoId, using the alias name CrgoId.
     * @return the value of CrgoId
     */
    public String getCrgoId() {
        return (String)getAttributeInternal(CRGOID);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrgoId.
     * @param value value to set the CrgoId
     */
    public void setCrgoId(String value) {
        setAttributeInternal(CRGOID, value);
    }

    /**
     * Gets the attribute value for CrgoNombre, using the alias name CrgoNombre.
     * @return the value of CrgoNombre
     */
    public String getCrgoNombre() {
        return (String)getAttributeInternal(CRGONOMBRE);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrgoNombre.
     * @param value value to set the CrgoNombre
     */
    public void setCrgoNombre(String value) {
        setAttributeInternal(CRGONOMBRE, value);
    }

    /**
     * Gets the attribute value for CrgoMetaCargo, using the alias name CrgoMetaCargo.
     * @return the value of CrgoMetaCargo
     */
    public String getCrgoMetaCargo() {
        return (String)getAttributeInternal(CRGOMETACARGO);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrgoMetaCargo.
     * @param value value to set the CrgoMetaCargo
     */
    public void setCrgoMetaCargo(String value) {
        setAttributeInternal(CRGOMETACARGO, value);
    }

    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index, AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }

    /**
     * @return the associated entity oracle.jbo.RowIterator.
     */
    public RowIterator getListaSkillEdit() {
        return (RowIterator)getAttributeInternal(LISTASKILLEDIT);
    }

    /**
     * @param crgoId key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(String crgoId) {
        return new Key(new Object[]{crgoId});
    }


}
