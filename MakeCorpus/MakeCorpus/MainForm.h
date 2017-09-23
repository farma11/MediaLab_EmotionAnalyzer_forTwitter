#pragma once

namespace MakeCorpus {

	using namespace System;
	using namespace System::ComponentModel;
	using namespace System::Collections;
	using namespace System::Windows::Forms;
	using namespace System::Data;
	using namespace System::Drawing;

	/// <summary>
	/// MainForm の概要
	/// </summary>
	public ref class MainForm : public System::Windows::Forms::Form
	{
	public:
		MainForm(void)
		{
			InitializeComponent();
			//
			//TODO: ここにコンストラクター コードを追加します
			//
		}

	protected:
		/// <summary>
		/// 使用中のリソースをすべてクリーンアップします。
		/// </summary>
		~MainForm()
		{
			if (components)
			{
				delete components;
			}
		}
	private: System::Windows::Forms::TabControl^  tabControl1;
	protected:
	private: System::Windows::Forms::TabPage^  tabPage1;
	private: System::Windows::Forms::PictureBox^  pictureBoxMain;
	private: System::Windows::Forms::TabPage^  tabPage2;
	private: System::Windows::Forms::Button^  buttonShowPic;
	private: System::Windows::Forms::TextBox^  textBoxPicName;
	private: System::Windows::Forms::OpenFileDialog^  openFileDialogImage;


	private:
		/// <summary>
		/// 必要なデザイナー変数です。
		/// </summary>
		System::ComponentModel::Container ^components;

#pragma region Windows Form Designer generated code
		/// <summary>
		/// デザイナー サポートに必要なメソッドです。このメソッドの内容を
		/// コード エディターで変更しないでください。
		/// </summary>
		void InitializeComponent(void)
		{
			this->tabControl1 = (gcnew System::Windows::Forms::TabControl());
			this->tabPage1 = (gcnew System::Windows::Forms::TabPage());
			this->textBoxPicName = (gcnew System::Windows::Forms::TextBox());
			this->buttonShowPic = (gcnew System::Windows::Forms::Button());
			this->pictureBoxMain = (gcnew System::Windows::Forms::PictureBox());
			this->tabPage2 = (gcnew System::Windows::Forms::TabPage());
			this->openFileDialogImage = (gcnew System::Windows::Forms::OpenFileDialog());
			this->tabControl1->SuspendLayout();
			this->tabPage1->SuspendLayout();
			(cli::safe_cast<System::ComponentModel::ISupportInitialize^>(this->pictureBoxMain))->BeginInit();
			this->SuspendLayout();
			// 
			// tabControl1
			// 
			this->tabControl1->Controls->Add(this->tabPage1);
			this->tabControl1->Controls->Add(this->tabPage2);
			this->tabControl1->Dock = System::Windows::Forms::DockStyle::Bottom;
			this->tabControl1->Location = System::Drawing::Point(0, 23);
			this->tabControl1->Name = L"tabControl1";
			this->tabControl1->SelectedIndex = 0;
			this->tabControl1->Size = System::Drawing::Size(344, 326);
			this->tabControl1->TabIndex = 0;
			// 
			// tabPage1
			// 
			this->tabPage1->Controls->Add(this->textBoxPicName);
			this->tabPage1->Controls->Add(this->buttonShowPic);
			this->tabPage1->Controls->Add(this->pictureBoxMain);
			this->tabPage1->Location = System::Drawing::Point(4, 22);
			this->tabPage1->Name = L"tabPage1";
			this->tabPage1->Padding = System::Windows::Forms::Padding(3);
			this->tabPage1->Size = System::Drawing::Size(336, 300);
			this->tabPage1->TabIndex = 0;
			this->tabPage1->Text = L"ファイル";
			this->tabPage1->UseVisualStyleBackColor = true;
			// 
			// textBoxPicName
			// 
			this->textBoxPicName->Location = System::Drawing::Point(9, 23);
			this->textBoxPicName->Name = L"textBoxPicName";
			this->textBoxPicName->Size = System::Drawing::Size(238, 19);
			this->textBoxPicName->TabIndex = 2;
			this->textBoxPicName->Text = L"./images/pic1.png";
			// 
			// buttonShowPic
			// 
			this->buttonShowPic->Location = System::Drawing::Point(253, 19);
			this->buttonShowPic->Name = L"buttonShowPic";
			this->buttonShowPic->Size = System::Drawing::Size(75, 23);
			this->buttonShowPic->TabIndex = 1;
			this->buttonShowPic->Text = L"参照";
			this->buttonShowPic->UseVisualStyleBackColor = true;
			this->buttonShowPic->Click += gcnew System::EventHandler(this, &MainForm::buttonShowPic_Click);
			// 
			// pictureBoxMain
			// 
			this->pictureBoxMain->Location = System::Drawing::Point(8, 52);
			this->pictureBoxMain->Name = L"pictureBoxMain";
			this->pictureBoxMain->Size = System::Drawing::Size(320, 240);
			this->pictureBoxMain->TabIndex = 0;
			this->pictureBoxMain->TabStop = false;
			// 
			// tabPage2
			// 
			this->tabPage2->Location = System::Drawing::Point(4, 22);
			this->tabPage2->Name = L"tabPage2";
			this->tabPage2->Padding = System::Windows::Forms::Padding(3);
			this->tabPage2->Size = System::Drawing::Size(336, 300);
			this->tabPage2->TabIndex = 1;
			this->tabPage2->Text = L"tabPage2";
			this->tabPage2->UseVisualStyleBackColor = true;
			// 
			// openFileDialogImage
			// 
			this->openFileDialogImage->FileName = L"openFileDialog1";
			this->openFileDialogImage->Filter = L"JPEG|*.jpg|GIF|*.gif|PNG|*.png|ALL|*.*";
			// 
			// MainForm
			// 
			this->AutoScaleDimensions = System::Drawing::SizeF(6, 12);
			this->AutoScaleMode = System::Windows::Forms::AutoScaleMode::Font;
			this->ClientSize = System::Drawing::Size(344, 349);
			this->Controls->Add(this->tabControl1);
			this->Name = L"MainForm";
			this->Text = L"MakeCorpus";
			this->tabControl1->ResumeLayout(false);
			this->tabPage1->ResumeLayout(false);
			this->tabPage1->PerformLayout();
			(cli::safe_cast<System::ComponentModel::ISupportInitialize^>(this->pictureBoxMain))->EndInit();
			this->ResumeLayout(false);

		}
#pragma endregion
	private: System::Void buttonShowPic_Click(System::Object^  sender, System::EventArgs^  e) {
			//pictureBoxに画像を指定
			//pictureBoxMain->ImageLocation = textBoxPicName->Text;

				 if (openFileDialogImage->ShowDialog() == Windows::Forms::DialogResult::OK)
				 {
					 String^ fileName = openFileDialogImage->FileName;
					 pictureBoxMain->ImageLocation = fileName;
					 textBoxPicName->Text = fileName;
				 }
	}
private: System::Void pictureBoxMain_Click(System::Object^  sender, System::EventArgs^  e) {
}
};
}
