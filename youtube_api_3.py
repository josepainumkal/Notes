from apiclient.discovery import build
from apiclient.errors import HttpError
from oauth2client.tools import argparser

import requests


# Set DEVELOPER_KEY to the API key value from the APIs & auth > Registered apps
# tab of
#   https://cloud.google.com/console
# Please ensure that you have enabled the YouTube Data API for your project.
DEVELOPER_KEY = "AIzaSyAlwO5OTlNmsr7xjOx7c-IilXr3NaTD_o0"
YOUTUBE_API_SERVICE_NAME = "youtube"
YOUTUBE_API_VERSION = "v3"

def youtube_search(options):
  youtube = build(YOUTUBE_API_SERVICE_NAME, YOUTUBE_API_VERSION,
    developerKey=DEVELOPER_KEY)

  # Call the search.list method to retrieve results matching the specified
  # query term.
  search_response = youtube.search().list(
    q=options.q,
    part="id,snippet",
    maxResults=options.max_results
  ).execute()

  videos = []
  channels = []
  playlists = []

  # Add each result to the appropriate list, and then display the lists of
  # matching videos, channels, and playlists.
  # print search_response
  for search_result in search_response.get("items", []):
    if search_result["id"]["kind"] == "youtube#video":
      # videos.append("%s (%s)" % (search_result["snippet"]["title"],
      #                            search_result["id"]["videoId"]))

      payload = {'id': search_result["id"]["videoId"], 'part': 'statistics', 'key': DEVELOPER_KEY}
      # payload = {'id': search_result["id"]["videoId"], 'part': 'contentDetails,statistics,snippet', 'key': DEVELOPER_KEY}
      l = requests.Session().get('https://www.googleapis.com/youtube/v3/videos', params=payload)    
      print l.text



  print "Videos:\n", "\n".join(videos), "\n"


if __name__ == "__main__":
  argparser.add_argument("--q", help="Search term", default="Malayalam")
  argparser.add_argument("--max-results", help="Max results", default=2)
  args = argparser.parse_args()

  print "hello my args are: ",args

  try:
    youtube_search(args)
  except HttpError, e:
    print "A HTTP error %d occurred:\n%s" % (e.resp.status, e.content)
