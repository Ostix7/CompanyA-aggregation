# Project Name: Aggregation and Analytics Service
## Overview
This project provides an advanced aggregation and analytics service focused on extracting, analyzing, and visualizing data from YouTube and Telegram. Leveraging BigQuery configurations, our service fetches detailed information from these platforms, performs scheduled analytics tasks, and presents a comprehensive set of metrics through a user-friendly dashboard powered by AWS-managed Grafana.

## How We Work
Our process can be broken down into several key steps:

Data Fetching: Utilizing two BigQuery configurations, we extract data from YouTube and Telegram Team accounts. This step is crucial for gathering the raw data required for subsequent analytics.

Analytics: Once the data is fetched, we perform scheduled analytics tasks to derive insightful metrics from the raw data.

Visualization: The processed metrics are visualized using AWS-managed Grafana, offering a clear and interactive way to interpret the analytics results.

## Metrics
Our service generates a wide range of metrics for both YouTube and Telegram, including but not limited to:

#### For Telegram:
- Number of posts in each channel
- Most active commenters
- Quantitative distribution of media types in posts
- Average number of views per channel post
- Distribution of posts by publication date
- Top posts by number of reactions
- Posts with the most diverse reactions
- Most popular emojis
- Number of reactions per post
#### For YouTube:
- Usage of specific languages in videos
- Popularity of languages by subtitles
- Most popular channels by number of subscribers
- Number of comments per video
- Most active commenters
- Most popular videos by number of likes
- Number of videos per channel
- Average number of video views per channel
- Number of videos by tags
- Distribution of videos by publication date

In addition to these platform-specific metrics, we also perform topic modeling and semantic analysis for both YouTube and Telegram to uncover deeper insights into the content.

## Technology Stack
Our project utilizes a robust technology stack including:

- Spring Boot and Java for backend development
- AWS for cloud services and storage
- RDS (PostgreSQL) for database management
- AWS Managed Grafana for data visualization

## Hosting Location
Our project is hosted in Frankfurt to leverage geographic and regulatory advantages, ensuring better performance and compliance for users in the European Union.

## Grafana Instructions
Our analytics results are visualized using AWS Managed Grafana. Here are the initial steps to access the Grafana dashboard:

To be completed

## Getting Started
You can run your Spring Boot application: java -jar target/aggregation-0.0.1-SNAPSHOT.jar
