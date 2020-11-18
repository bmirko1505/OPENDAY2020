<html>

<body>

    <?php
    $txt = $_POST["outputXMLPHP"];
    $tmpfname = tempnam("../outSiteInGelataio", "outSiteInGelataio");
    rename($tmpfname, $tmpfname .= '.xml');
    $myfile = fopen($tmpfname, "w") or die("Unable to open file!");
    fwrite($myfile, $txt);
    fclose($myfile);

    echo "<script>window.close()</script>";
    ?>

</body>

</html>