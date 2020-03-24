<Image>
	<messageId>${reqNo}</messageId>
	<ImageFileList>
		<#list imageFiles as imageFile>
			<ImageFile>
				<FileName>${imageFile.fileName}</FileName>
				<FileMessage>${imageFile.base64Str}</FileMessage>
			</ImageFile>
		</#list>
	</ImageFileList>
</Image>