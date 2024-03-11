import os
import boto3
import paramiko

# S3 Client
s3 = boto3.client('s3')

# Config
sftp_config = {
    'hostname': '194.44.143.234',
    'port': 2442,
    'username': 'group1',
    'password': 'eDBsJVqJxPsdSJGoAAmy',
}

folders_to_buckets = {
    'youtube': 'our-team-s3-bucket-name-for-youtube',
    'telegram': 'our-team-s3-bucket-name-for-telegram',
}

def lambda_handler(event, context):
    transport = paramiko.Transport((sftp_config['hostname'], sftp_config['port']))
    transport.connect(username=sftp_config['username'], password=sftp_config['password'])
    sftp = paramiko.SFTPClient.from_transport(transport)

    for folder, bucket_name in folders_to_buckets.items():
        try:
            sftp.chdir(folder)
            files = sftp.listdir()

            for filename in files:
                local_path = f'/tmp/{filename}'
                with open(local_path, 'wb') as f:
                    sftp.getfo(filename, f)

                with open(local_path, 'rb') as f:
                    s3.upload_fileobj(f, bucket_name, filename)

        except Exception as e:
            print(f"Error working with {folder}: {e}")
            continue

    sftp.close()
    transport.close()
    return {
        'statusCode': 200,
        'body': 'Successfully transferred files from SFTP to corresponding S3 buckets'
    }