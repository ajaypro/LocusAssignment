package com.deepak.locus.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.deepak.locus.R
import com.deepak.locus.data.model.DataItem
import com.squareup.picasso.Picasso

internal class ImageViewHolder(itemView: View, private val clickListener: OnItemClickListener): BaseViewHolder(itemView), View.OnClickListener{

    var imageView: ImageView = itemView.findViewById(R.id.image_view)
    var clearImage: ImageButton = itemView.findViewById(R.id.btn_clear)
    lateinit var dataItem: DataItem

    init {
       imageView.setOnClickListener(this)
        clearImage.setOnClickListener(this)
    }

    override fun bindView(item: DataItem) {
        dataItem = item
        val uri = if(item.dataMap?.has("imagePath")!!)
         item.dataMap.get("imagePath")?.asString  else "null"
        Picasso.get().load(uri).into(imageView)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.image_view) {
         clickListener.onItemClick(adapterPosition, dataItem)
        } else if ( view?.id == R.id.btn_clear) {
            clickListener.onCrossClick(adapterPosition, dataItem)
        }
    }

}

internal class ChoiceViewHolder(itemView: View): BaseViewHolder(itemView), RadioGroup.OnCheckedChangeListener{

    var radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
    var radioTitle: TextView = itemView.findViewById(R.id.radio_title)
    lateinit var dataItem: DataItem

    init {
       radioGroup.setOnCheckedChangeListener(this)
    }

    override fun bindView(item: DataItem) {
        dataItem = item
        radioTitle.text = item.title
          val options = item.dataMap?.getAsJsonArray("options")
        radioGroup.removeAllViews()

        var selected = -1
        if(item.dataMap!!.has("selectedOption"))
            selected = item.dataMap.get("selectedOption").asInt

        radioGroup.clearCheck()
        options?.let {
            for(i in 0 until options.size()){
                RadioButton(itemView.context).apply {
                    text = options[i].asString
                    tag = i
                    radioGroup.addView(this, RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                    isChecked = (i == selected)
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val radioButton = group?.findViewById<RadioButton>(checkedId) ?: return
        val tag = radioButton.tag as Int
            dataItem.dataMap?.addProperty("selectedOption", tag)
        if(checkedId != -1){
            dataItem.currentValue = "  $checkedId"
        } else {
            dataItem.currentValue = "-1"
        }
    }
}

internal class CommentViewHolder(itemView: View): BaseViewHolder(itemView),
    CompoundButton.OnCheckedChangeListener, TextWatcher {

    var commentView: EditText = itemView.findViewById(R.id.edt_comment)
    var commentTitle: TextView = itemView.findViewById(R.id.comment_title)
    var switchButton: Switch = itemView.findViewById(R.id.comment_swtich_btn)
    private lateinit var dataItem: DataItem

    init {
        commentView.addTextChangedListener(this)
        switchButton.setOnCheckedChangeListener(this)
    }

    override fun bindView(item: DataItem) {
        dataItem = item
        commentTitle.text = item.title
        if(item.dataMap!!.has("text")){
            commentView.setText(item.dataMap.get("text").asString)
        } else {
            commentView.text = null
        }
        if(item.dataMap.has("checked")){
            switchButton.isChecked = item.dataMap.get("checked").asBoolean
        } else {
            switchButton.isChecked = false
        }

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        dataItem.dataMap?.addProperty("checked", isChecked)
        if(isChecked){
            commentView.visibility = View.VISIBLE
            dataItem.currentValue = commentView.text.toString()
        } else {
            dataItem.currentValue = null
            commentView.visibility = View.GONE
        }
    }

    override fun afterTextChanged(s: Editable?) {
        dataItem.dataMap!!.addProperty("text", s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}