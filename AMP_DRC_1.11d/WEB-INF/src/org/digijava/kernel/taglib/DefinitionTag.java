/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.taglib.tiles.PutListTag;
import org.apache.struts.taglib.tiles.PutTag;
import org.apache.struts.tiles.AttributeDefinition;
import org.apache.struts.tiles.UntyppedAttribute;
import org.digijava.kernel.request.RequestProcessor;

/**
 * @author wb231862
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DefinitionTag extends org.apache.struts.taglib.tiles.DefinitionTag
{

	/**
	 * Process nested &lg;put&gt; tag.
	 * Method is called from nested &lg;put&gt; tags.
	 * Nested list is added to current list.
	 * If role is defined, nested attribute is wrapped into an untyped definition
	 * containing attribute value and role.
	 */
  public void processNestedTag(PutTag nestedTag) throws JspException
   {
	  // Get real value and check role
	  // If role is set, add it in attribute definition if any.
	  // If no attribute definition, create untyped one and set role.
	Object attributeValue = nestedTag.getRealValue();
	AttributeDefinition def;

	if( nestedTag.getRole() != null )
	  {
	  try
		{
		def = ((AttributeDefinition)attributeValue);
		}
	   catch( ClassCastException ex )
		{
		def = new UntyppedAttribute( attributeValue );
		}
	  def.setRole(nestedTag.getRole());
	  attributeValue = def;
	  } // end if
	  // now add attribute to enclosing parent (i.e. : this object)

          // Commented by L.D.
//	putAttribute( nestedTag.getName(), RequestProcessor.changeSitePath((HttpServletRequest) pageContext.getRequest(),(String)attributeValue));
	}

	/**
	 * Process nested &lg;putList&gt; tag.
	 * Method is called from nested &lg;putList&gt; tags.
	 * Nested list is added to current list.
	 * If role is defined, nested attribute is wrapped into an untyped definition
	 * containing attribute value and role.
	 */
  public void processNestedTag(PutListTag nestedTag) throws JspException
   {
	  // Get real value and check role
	  // If role is set, add it in attribute definition if any.
	  // If no attribute definition, create untyped one and set role.
	Object attributeValue = nestedTag.getList();

	if( nestedTag.getRole() != null )
	  {
	  AttributeDefinition  def = new UntyppedAttribute( attributeValue );
	  def.setRole(nestedTag.getRole());
	  attributeValue = def;
	  } // end if
	  // Check if a name is defined
	if( nestedTag.getName() == null)
	  throw new JspException( "Error - PutList : attribute name is not defined. It is mandatory as the list is added to a 'definition'." );
	  // now add attribute to enclosing parent (i.e. : this object).
	putAttribute(nestedTag.getName(), attributeValue);
	}

}