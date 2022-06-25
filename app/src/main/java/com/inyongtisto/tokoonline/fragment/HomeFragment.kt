package com.inyongtisto.tokoonline.fragment


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inyongtisto.tokoonline.BuildConfig
import com.inyongtisto.tokoonline.R
import com.inyongtisto.tokoonline.activity.PdfActivity
import com.inyongtisto.tokoonline.activity.signature
import com.inyongtisto.tokoonline.helper.SharedPref
import com.inyongtisto.tokoonline.model.Laporan
import com.inyongtisto.tokoonline.model.code
import com.inyongtisto.tokoonline.networking.ApiConfig
import com.inyongtisto.tokoonline.networking.AppConfig
import com.inyongtisto.tokoonline.networking.ServerResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.pb
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class HomeFragment : Fragment(), View.OnClickListener {
    var jenis = arrayOf("PENGELUARAN","PEMASUKAN","PEKERJAAN")
    var languages = arrayOf("KAS BESAR","KAS KECIL")
    private lateinit var imageView: ImageView
    private lateinit var CaptureImage: Button
    private lateinit var btnmake: Button
    private lateinit var upload: Button
    private val mMediaUri: Uri? = null
    private lateinit var harga: EditText
    private lateinit var deskripsi: EditText
    private lateinit var code: TextView
    private lateinit var paymentmethod: TextView
    private lateinit var jenislaporan: Spinner
    private lateinit var jeniskas: Spinner
    private var fileUri: Uri? = null
    var str:String = ""
    private var mediaPath: String? = null
    lateinit var s: SharedPref
    private val btnCapturePicture: Button? = null
    private var current: String = ""
    private var mImageFileLocation = ""
    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        getcode()


        val spinner1: Spinner = view.findViewById(R.id.spinner1)
        val spinner2: Spinner = view.findViewById(R.id.spinner2)
        code = view.findViewById(R.id.code)
        paymentmethod = view.findViewById(R.id.payment_amount)
        jenislaporan = view.findViewById(R.id.spinner1)
        jeniskas = view.findViewById(R.id.spinner2)
        harga = view.findViewById(R.id.harga)
        btnmake=view.findViewById(R.id.btnmake)
        deskripsi = view.findViewById(R.id.deskripsi)
        val Hours: TextView = view.findViewById(R.id.hour)
        val textView: TextView = view.findViewById(R.id.dateAndTime)
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
        val simpleDateFormathours = SimpleDateFormat("hh:mm:ss")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        val hours: String = simpleDateFormathours.format(Date())
        textView.text = currentDateAndTime
        Hours.text = hours

        s = activity?.let { SharedPref(it) }!!








        harga.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                paymentmethod.setText("Rp."+s)
            }


        })


        imageView = view.findViewById(R.id.imgCamera) as ImageView
        CaptureImage = view.findViewById(R.id.btncamera) as Button
        CaptureImage.setOnClickListener(this)
        initDialog()

        btnmake.setOnClickListener {

          makelaporan()
        }
        val openttd = view.findViewById(R.id.userttd) as ImageView
        val uploadb = view.findViewById(R.id.uploadb) as Button
        openttd.setOnClickListener {

            val intent = Intent(context, signature::class.java);
            startActivity(intent);
        }
        uploadb.setOnClickListener {
            val intent = Intent(context, PdfActivity::class.java);
            startActivity(intent);
        }
        spinner1?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, jenis) } as SpinnerAdapter
        spinner1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("ERROR")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
               // Toast.makeText(activity, type, Toast.LENGTH_LONG).show()
            //    println(type)
            }
        }


        spinner2?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, languages) } as SpinnerAdapter
        spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("ERROR")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
               // Toast.makeText(activity, type, Toast.LENGTH_LONG).show()
              //  println(type)
            }
        }


            return view
        }

        fun init(view: View) {

        }







 private fun getcode(){

    var call=   com.inyongtisto.tokoonline.app.ApiConfig.instanceRetrofit.getcodeLaporan()

    call.enqueue(object : Callback<code> {

        override fun onResponse(call: Call<code>?, response: Response<code>?) {
            val respon = response?.body()!!

            var data = respon.codelaporan


                str =data

            code!!.text = str


        }

        override fun onFailure(call: Call<code>, t: Throwable) {
            Toast.makeText(activity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
        }
    })
 }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btncamera -> captureImage()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = activity?.contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))

                    cursor.close()


                    postPath = mediaPath

                }


            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {

                    Glide.with(this).load(mImageFileLocation).into(imageView)
                    postPath = mImageFileLocation
uploadFile()
                } else {
                    Glide.with(this).load(fileUri).into(imageView)
                    postPath = fileUri!!.path

                }

            }

        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(activity, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }

    protected fun initDialog() {

        pDialog = ProgressDialog(activity)
        pDialog.setMessage(getString(R.string.msg_loading))
        pDialog.setCancelable(true)
    }


    protected fun showpDialog() {

        if (!pDialog.isShowing) pDialog.show()
    }

    protected fun hidepDialog() {

        if (pDialog.isShowing) pDialog.dismiss()
    }


    /**
     * Launching camera app to capture image
     */
    private fun captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            val callCameraApplicationIntent = Intent()
            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

            // We give some instruction to the intent to save the image
            var photoFile: File? = null

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile()

                // Here we call the function that will try to catch the exception made by the throw function
            } catch (e: IOException) {
                Logger.getAnonymousLogger().info("Exception error in generating the file")
                e.printStackTrace()
            }

            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            val outputUri = FileProvider.getUriForFile(requireContext(),

                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile!!)
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

            Logger.getAnonymousLogger().info("Calling the camera App by intent")

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }

    }

    @Throws(IOException::class)
    internal fun createImageFile(): File {
        Logger.getAnonymousLogger().info("Generating the image - method started")

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_" + timeStamp
        // Here we specify the environment location and the exact path where we want to save the so-created file
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
        Logger.getAnonymousLogger().info("Storage directory set")

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir()

        // Here we create the file using a prefix, a suffix and a directory
        val image = File(storageDirectory, imageFileName + ".jpg")
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set")

        mImageFileLocation = image.absolutePath
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application

        return image

    }

private fun makelaporan(){
    val spinnertxt1: String = jenislaporan.getSelectedItem().toString()
    val spinnertxt2: String = jeniskas.getSelectedItem().toString()
    if (harga.text.isEmpty()) {
        harga.error = "Kolom tidak boleh kosong"
        harga.requestFocus()
        return
    } else if (deskripsi.text.isEmpty()) {
        deskripsi.error = "Kolom tidak boleh kosong"
        deskripsi.requestFocus()
        return
    }

    pb.visibility = View.VISIBLE

    com.inyongtisto.tokoonline.app.ApiConfig.instanceRetrofit.laporanmake(code.text.toString(),s.user,s.level,spinnertxt1,spinnertxt2,harga.text.toString(),deskripsi.text.toString()).enqueue(object : Callback<Laporan> {
        override fun onFailure(call: Call<Laporan>, t: Throwable) {
            pb.visibility = View.GONE
            Toast.makeText(activity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: Call<Laporan>, response: Response<Laporan>) {
            pb.visibility = View.GONE
            val respon = response.body()!!
            if (respon.success == 1){



                Toast.makeText(activity, "Sukses Buat Laporan"+respon.laporan, Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(activity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()
            }
        }
    })
}


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri)

    }




    /**
     * Receiving activity result method will be called after closing the camera
     */

    /**
     * ------------ Helper Methods ----------------------
     */

    /**
     * Creating file uri to store image/video
     */
    fun getOutputMediaFileUri(type: Int): Uri = Uri.fromFile(getOutputMediaFile(type))

    private fun uploadFile() {
        if (postPath == null || postPath == "") {
            Toast.makeText(activity, "please select an image ", Toast.LENGTH_LONG).show()
            return
        } else {
            showpDialog()

            // Map is used to multipart the file using okhttp3.RequestBody
            val map = HashMap<String, RequestBody>()
            val file = File(postPath!!)

            // Parsing any Media type file
            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
            map.put("file\"; filename=\"" + file.name + "\"", requestBody)
            val getResponse = AppConfig.getRetrofit().create(ApiConfig::class.java)
            val call = getResponse.upload("token", map)
            call.enqueue(object : Callback<ServerResponse> {
                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            hidepDialog()
                            val serverResponse = response.body()
                            Toast.makeText(activity, serverResponse!!.message, Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        hidepDialog()
                        Toast.makeText(activity, "problem uploading image", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    hidepDialog()
                    Log.v("Response gotten is", t.message)
                }
            })
        }
    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_PICK_PHOTO = 2
        private val CAMERA_PIC_REQUEST = 1111

        private val TAG = HomeFragment::class.java.getSimpleName()

        private val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100

        val MEDIA_TYPE_IMAGE = 1
        val IMAGE_DIRECTORY_NAME = "Android File Upload"

        /**
         * returning image / video
         */
        private fun getOutputMediaFile(type: Int): File? {
            // External sdcard location
            val mediaStorageDir = File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME)

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory")
                    return null
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(mediaStorageDir.path + File.separator
                        + "IMG_" + ".jpg")
            } else {
                return null
            }

            return mediaFile
        }

    }

}




