<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${nombre}</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 48px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

    <table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
        <tr>
            <td align="center" bgcolor="#16A085" style="padding: 40px 0 30px 0;">               
                <p style="color:#F7F9F9";> Hi ${nombre} ${apellidos}! </p>                
            </td>
        </tr>
        <tr>
            <td bgcolor="#eaeaea" style="padding: 40px 30px 40px 30px;">
             <p>
             We have received the request to recover your ${email} account from CityTrash. If you have not requested, please do not access the following link <a href="${recuperar_url}">${recuperar_url}</a> and ignore the message.
			</p>
			
			<p>	
				Remember that you must recover your account before ${fechaExpiracion}
			</p>         
			                                 
            </td>
        </tr>
        <tr>
            <td bgcolor="#777777" style="padding: 30px 30px 30px 30px;">
                <p> Thanks, </p>
                <p style="color:#F7F9F9";>${firma}</p>               
            </td>
        </tr>
    </table>

</body>
</html>



