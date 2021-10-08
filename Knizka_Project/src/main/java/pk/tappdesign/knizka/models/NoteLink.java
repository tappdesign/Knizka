/*
 * Copyright (C) 2020 TappDesign Studios
 * Copyright (C) 2013-2020 Federico Iosue (federico@iosue.it)
 *
 * This software is based on Omni-Notes project developed by Federico Iosue
 * https://github.com/federicoiosue/Omni-Notes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pk.tappdesign.knizka.models;

public class NoteLink {

   private long linkID;
   private long handleIDRef;
   private long textIdRef;
   private int textOrder;
   private int textType;
   private int category;
   private String textRefCaption;
   private String categoryRefCaption;

   public NoteLink() {
      linkID = 0;
      handleIDRef = 0;
      textIdRef = 0;
      textOrder = 0;
      textType = 0;
      category = 0;
      textRefCaption = "";
      categoryRefCaption = "";
   }

   public long getLinkID() {
      return linkID;
   }

   public void setLinkID(long linkID) {
      this.linkID = linkID;
   }

   public long getHandleIDRef() {
      return handleIDRef;
   }

   public void setHandleIDRef(long handleIDRef) {
      this.handleIDRef = handleIDRef;
   }

   public long getTextIdRef() {
      return textIdRef;
   }

   public void setTextIdRef(long textIdRef) {
      this.textIdRef = textIdRef;
   }

   public int getTextOrder() {
      return textOrder;
   }

   public void setTextOrder(int textOrder) {
      this.textOrder = textOrder;
   }

   public int getTextType() {
      return textType;
   }

   public void setTextType(int textType) {
      this.textType = textType;
   }

   public int getCategory() {
      return category;
   }

   public void setCategory(int category) {
      this.category = category;
   }

   public String getTextRefCaption() {
      return textRefCaption;
   }

   public void setTextRefCaption(String textRefCaption) {
      this.textRefCaption = textRefCaption;
   }

   public String getCategoryRefCaption() {
      return categoryRefCaption;
   }

   public void setCategoryRefCaption(String categoryRefCaption) {
      this.categoryRefCaption = categoryRefCaption;
   }
}
