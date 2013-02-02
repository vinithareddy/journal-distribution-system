datafolder_in = "C:\Users\smahapat\Desktop\Jan2013"
datafolder_out = datafolder_in & "\out"
set fso = CreateObject("Scripting.FileSystemObject")
set excel = createobject("excel.application")

If Not fso.FolderExists(datafolder_out) Then
   Set newfolder = fso.CreateFolder(datafolder_out)
   wscript.echo("A new folder has been created at: " & datafolder_out)
End If
excel.DefaultFilePath = datafolder_out
files = Array("CLIFESUB", _
              "CORR", _
              "CURREB", _
              "CURRFEX", _
              "CURRGRAN", _
              "CURRIEX", _
              "CURRMEM", _
              "CURRWC", _
              "CURTWAS", _
              "EBALL", _
              "EXCHANGE", _
              "HON", _
              "HONFEL", _
              "JGRANT", _
              "JNLS", _
              "MEMBER", _
              "RESOCOMP")
for each file in files
    wscript.echo("Exporting file " & file)
    Set objWB = excel.WorkBooks.Open(datafolder_in & "\" & file & ".DBF")
    objWB.SaveAs datafolder_out &  "\" & file & ".xls", 56
next
excel.Quit()
