import requests
from bs4 import BeautifulSoup
from tenacity import retry, stop_after_attempt, wait_fixed

url = 'https://unicode.org/emoji/charts/full-emoji-list.html'

@retry(stop=stop_after_attempt(5), wait=wait_fixed(2))
def get_response(url):
    response = requests.get(url)
    response.raise_for_status()  # This will raise an HTTPError if the HTTP request returned an unsuccessful status code
    return response

try:
    response = get_response(url)
    soup = BeautifulSoup(response.content, 'html.parser')

    emoji_map = {}

    for row in soup.find_all('tr'):
        columns = row.find_all('td')
        if len(columns) > 8:
            emoji = columns[2].text.strip()  # The column with the emoji character
            name = columns[8].text.strip()  # The column with the emoji description
            emoji_map[name] = emoji

    # Save the emoji_map to a JSON file
    import json
    with open('emoji_map.json', 'w', encoding='utf-8') as f:
        json.dump(emoji_map, f, ensure_ascii=False, indent=4)

    print("Emoji map saved to emoji_map.json")

except requests.exceptions.RequestException as e:
    print(f"Error fetching data: {e}")

