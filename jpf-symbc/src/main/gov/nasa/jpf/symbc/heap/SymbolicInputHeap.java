/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

//
//Copyright (C) 2006 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.
//
//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.
//
//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//

package gov.nasa.jpf.symbc.heap;

import java.util.ArrayList;

import gov.nasa.jpf.symbc.arrays.ArrayHeapNode;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.vm.ClassInfo;


public class SymbolicInputHeap {

    HeapNode header;
    int count = 0;

    public SymbolicInputHeap() {
    	header = null;
    }

	public SymbolicInputHeap make_copy() {
		SymbolicInputHeap sih_new = new SymbolicInputHeap();
		sih_new.header = this.header;
	    sih_new.count = this.count;
		return sih_new;
	}

	public void _add(HeapNode n) {

		if (!hasNode(n)) {
			n.setNext(header);
			header = n;
			count++;
		}

	}

	public int count() {
		return count;
	}

	public HeapNode header() {
		return header;
	}

	public boolean hasNode(HeapNode n) {
		HeapNode t = header;

		while (t != null) {
			if (n.equals(t)) {
				return true;
			}

			t = t.getNext();
		}

		return false;
	}
	
	public SymbolicInteger getNode(int daIndex) {
	    HeapNode n = header;
        while (n != null){
            if (n.getIndex() == daIndex)
                return n.getSymbolic();
            n = n.getNext();
        }
        return null;
	}
	
	public HeapNode[] getNodesOfType(ClassInfo type) {
		// 30-04-2020 Refactored using ArrayList instead of two passes: Hendrik Winkelmann
		  ArrayList<HeapNode> nodesOfType = new ArrayList<>();
		  HeapNode n = header;
		  while (null != n){
			  ClassInfo tClassInfo = n.getType();
			  if (tClassInfo.isInstanceOf(type)) {
				  nodesOfType.add(n);
			  }
			  n = n.getNext();
		  }
		  
		  return nodesOfType.toArray(new HeapNode[0]);
	}
	
	// Added getAllNodes: Hendrik Winkelmann
	public HeapNode[] getAllNodes() {
		ArrayList<HeapNode> nodes = new ArrayList<>();
		HeapNode n = header;
		while (n != null) {
			nodes.add(n);
			n = n.getNext();
		}
		return nodes.toArray(new HeapNode[0]);
	}

    public ArrayHeapNode[] getArrayNodesOfType(ClassInfo type, int ref) {
    	// 30-04-2020 Refactored using ArrayList instead of two passes: Hendrik Winkelmann
    	ArrayList<ArrayHeapNode> arrayNodesOfType = new ArrayList<>();
        HeapNode n = header;
        while (null != n) {
            if (n instanceof ArrayHeapNode) {
                ClassInfo tClassInfo = n.getType();
                if (tClassInfo.isInstanceOf(type)) {
                    if (((ArrayHeapNode)n).arrayRef == ref) {
                        arrayNodesOfType.add((ArrayHeapNode) n);
                    }
                }
            }
            n = n.getNext();
        }
        
        return arrayNodesOfType.toArray(new ArrayHeapNode[0]);
    }

	
	public String toString() {
		return "SymbolicInputHeap = " + count + ((header == null) ? "" : "\n" + header.toString());
	}

}
