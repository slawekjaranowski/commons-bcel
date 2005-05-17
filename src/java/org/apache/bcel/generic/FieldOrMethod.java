/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */ 
package org.apache.bcel.generic;

import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;

/**
 * Super class for InvokeInstruction and FieldInstruction, since they have
 * some methods in common!
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public abstract class FieldOrMethod extends CPInstruction implements LoadClass {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  FieldOrMethod() {}

  /**
   * @param index to constant pool
   */
  protected FieldOrMethod(short opcode, int index) {
    super(opcode, index);
  }

  /** @return signature of referenced method/field.
   */
  public String getSignature(ConstantPoolGen cpg) {
    ConstantPool        cp   = cpg.getConstantPool();
    ConstantCP          cmr  = (ConstantCP)cp.getConstant(index);
    ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(cmr.getNameAndTypeIndex());

    return ((ConstantUtf8)cp.getConstant(cnat.getSignatureIndex())).getBytes();
  }

  /** @return name of referenced method/field.
   */
  public String getName(ConstantPoolGen cpg) {
    ConstantPool        cp   = cpg.getConstantPool();
    ConstantCP          cmr  = (ConstantCP)cp.getConstant(index);
    ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(cmr.getNameAndTypeIndex());
    return ((ConstantUtf8)cp.getConstant(cnat.getNameIndex())).getBytes();
  }

  /** @return name of the referenced class/interface
   */
  public String getClassName(ConstantPoolGen cpg) {
    ConstantPool cp  = cpg.getConstantPool();
    ConstantCP   cmr = (ConstantCP)cp.getConstant(index);
    return cp.getConstantString(cmr.getClassIndex(), org.apache.bcel.Constants.CONSTANT_Class).replace('/', '.');
  }

  /** @return type of the referenced class/interface
   * @deprecated If the instruction references an array class,
   *    the ObjectType returned will be invalid.  Use
   *    getReferenceType() instead.
   */
  public ObjectType getClassType(ConstantPoolGen cpg) {
    return new ObjectType(getClassName(cpg));
  }

  /**
   * Return the reference type representing the class, interface,
   * or array class referenced by the instruction.
   * @param cpg the ConstantPoolGen used to create the instruction
   * @return an ObjectType (if the referenced class type is a class
   *   or interface), or an ArrayType (if the referenced class
   *   type is an array class)
   */
  public ReferenceType getReferenceType(ConstantPoolGen cpg) {
    ConstantPool cp  = cpg.getConstantPool();
    ConstantCP   cmr = (ConstantCP)cp.getConstant(index);
    String className = cp.getConstantString(cmr.getClassIndex(), org.apache.bcel.Constants.CONSTANT_Class);
    if (className.startsWith("[")) {
      return (ArrayType) Type.getType(className);
    } else {
      className = className.replace('/', '.');
      return new ObjectType(className);
    }
  }

  /** @return type of the referenced class/interface
   */
  public ObjectType getLoadClassType(ConstantPoolGen cpg) {
    return getClassType(cpg);
  }
}
